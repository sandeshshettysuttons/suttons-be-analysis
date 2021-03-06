package au.com.suttons.notification.service;


import au.com.suttons.notification.bean.EmployeeFileDetailBean;
import au.com.suttons.notification.data.dao.CompanyDao;
import au.com.suttons.notification.data.dao.EmployeeDao;
import au.com.suttons.notification.data.dao.EmployeeFileDao;
import au.com.suttons.notification.data.dao.EmployeeFileDetailDao;
import au.com.suttons.notification.data.entity.CompanyEntity;
import au.com.suttons.notification.data.entity.EmployeeEntity;
import au.com.suttons.notification.data.entity.EmployeeFileDetailEntity;
import au.com.suttons.notification.data.entity.EmployeeFileEntity;
import au.com.suttons.notification.fileprocessor.EmployeeTerminationFileProcessor;
import au.com.suttons.notification.jobs.JobConstants;
import au.com.suttons.notification.util.DateUtil;
import au.com.suttons.notification.util.FileUtil;
import au.com.suttons.notification.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Stateless
public class EmployeeFileService
{

    private static final Logger logger = LoggerFactory.getLogger(EmployeeFileService.class);

    @EJB
    private EmployeeFileDao employeeFileDao;

    @EJB
    private EmployeeFileDetailDao employeeFileDetailDao;

    @EJB
    private EmployeeDao employeeDao;

    @EJB
    private CompanyDao companyDao;

    public void readEmployeeFiles(Path file) throws IOException {

        try {

            String fileName = file.getFileName().toString();
            String fileUrl = file.toString();

            // Read file and insert details into Employee File table
            EmployeeTerminationFileProcessor processor = new EmployeeTerminationFileProcessor();
            List employeeFileDetails = processor.getList(fileUrl);

            if(CollectionUtils.isNotEmpty(employeeFileDetails)) {

                EmployeeFileEntity employeeFile = new EmployeeFileEntity();
                employeeFile.setFileUrl(fileUrl);
                employeeFile.setFileName(fileName);
                employeeFile.setStatus(JobConstants.STATUS_PROCESSED);
                employeeFile.setLastUpdatedBy(JobConstants.USER_SYSTEM);

                employeeFile = employeeFileDao.saveAndFlush(employeeFile);

                saveEmployeeFileDetails(employeeFileDetails, employeeFile);

                // If reading is successful, move file to imported directory
                moveFileToImportedDir(file);

            } else {
                // If not a standard employee file , move file to error directory
                moveFileToErrorDir(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error reading employee file  : "+ e.getMessage());

            // In case of exception, move file to error directory
            moveFileToErrorDir(file);
        }

    }

    public void saveEmployeeFileDetails(List<EmployeeFileDetailBean> employeeFileDetailList, EmployeeFileEntity employeeFile) {

        for (EmployeeFileDetailBean employeeDetail : employeeFileDetailList) {

            EmployeeFileDetailEntity entity = new EmployeeFileDetailEntity();

            entity.setEmployeeNumber(employeeDetail.getEmployeeNumber());
            entity.setFirstName(employeeDetail.getFirstName());
            entity.setLastName(employeeDetail.getLastName());
            entity.setPreferredName(employeeDetail.getPreferredName());
            entity.setReportName(employeeDetail.getReportName());
            entity.setCompanyCode(employeeDetail.getCompanyCode());
            entity.setDescription(employeeDetail.getDescription());
            entity.setPosition(employeeDetail.getPosition());
            if (StringUtils.isNotBlank(employeeDetail.getTerminationDate())) {
                entity.setTerminationDate(DateUtil.formatYYYY_MM_DD(employeeDetail.getTerminationDate()));
            }
            entity.setTerminationReason(employeeDetail.getTerminationReason());
            entity.setTerminationDescription(employeeDetail.getTerminationDescription());
            entity.setComment(employeeDetail.getComment());
            entity.setStatus(employeeDetail.getStatus());

            entity.setEmployeeFile(employeeFile);
            entity.setLastUpdatedBy(employeeFile.getLastUpdatedBy());

            entity = this.employeeFileDetailDao.saveAndFlush(entity);

            // Update Employee details
            updateEmployeeDetails(entity);
        }
    }

    public void updateEmployeeDetails(EmployeeFileDetailEntity employeeFileDetail) {

        EmployeeEntity employee = employeeDao.findByEmployeeNumber(employeeFileDetail.getEmployeeNumber());

        if (employee == null) {
            employee = new EmployeeEntity();
            employee.setNotificationSent(false);

        } else if ( (employee.getTerminationDate() != null && !employee.getTerminationDate().equals(employeeFileDetail.getTerminationDate()))
                    || (employee.getTerminationDate() == null && employeeFileDetail.getTerminationDate() != null) ) {

            employee.setNotificationSent(false);
        }

        employee.setEmployeeNumber(employeeFileDetail.getEmployeeNumber());
        employee.setFirstName(employeeFileDetail.getFirstName());
        employee.setLastName(employeeFileDetail.getLastName());
        employee.setPreferredName(employeeFileDetail.getPreferredName());
        employee.setReportName(employeeFileDetail.getReportName());
        employee.setDescription(employeeFileDetail.getDescription());
        employee.setPosition(employeeFileDetail.getPosition());
        employee.setTerminationDate(employeeFileDetail.getTerminationDate());
        employee.setTerminationReason(employeeFileDetail.getTerminationReason());
        employee.setTerminationDescription(employeeFileDetail.getTerminationDescription());
        employee.setComment(employeeFileDetail.getComment());
        employee.setStatus(JobConstants.STATUS_ACTIVE);

        employee.setLastUpdatedBy(employeeFileDetail.getLastUpdatedBy());

        employee.setCompany(
                updateCompanies(employee, employeeFileDetail.getCompanyCode()));

        this.employeeDao.saveAndFlush(employee);
    }

    public CompanyEntity updateCompanies(EmployeeEntity employee, String companyCode) {

        CompanyEntity company;

        if (StringUtils.isNumeric(companyCode)) {
            companyCode = String.valueOf(Integer.parseInt(companyCode));
            company = companyDao.findByCode(companyCode);

        } else {
            company = companyDao.findByName(employee.getDescription());
        }

        if (company == null) {
            company = new CompanyEntity();
            company.setCode(companyCode);
            company.setName(employee.getDescription());
            company.setStatus(JobConstants.STATUS_ACTIVE);
            company.setLastUpdatedBy(employee.getLastUpdatedBy());

            company = this.companyDao.saveAndFlush(company);
        }

        return company;
    }

    private void moveFileToImportedDir(Path file) {

        String originalFileName = file.getFileName().toString();
        String newFileName =
                new StringBuffer(originalFileName.substring(0 , originalFileName.lastIndexOf('.')))
                        .append("_")
                        .append(DateUtil.getFileTimestamp())
                        .append(originalFileName.substring(originalFileName.lastIndexOf('.')))
                        .toString();

        Path importedDir = Paths.get(
                FileUtil.getImportedEmployeeFilesLocation(), newFileName);

        Path temp = null;
        try {
            temp = Files.move(file, importedDir, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error moving file to imported directory : "+ e.getMessage());
        }

        if(temp == null) {
            logger.error("Error moving file to imported directory.");
        }
    }

    private void moveFileToErrorDir(Path file) {

        String originalFileName = file.getFileName().toString();
        String newFileName =
                new StringBuffer(originalFileName.substring(0 , originalFileName.lastIndexOf('.')))
                        .append("_")
                        .append(DateUtil.getFileTimestamp())
                        .append(originalFileName.substring(originalFileName.lastIndexOf('.')))
                        .toString();

        Path importedDir = Paths.get(
                FileUtil.getErrorEmployeeFilesLocation(), newFileName);

        Path temp = null;
        try {
            temp = Files.move(file, importedDir, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error moving file to error directory : "+ e.getMessage());
        }

        if(temp == null) {
            logger.error("Error moving file to error directory.");
        }
    }

}
