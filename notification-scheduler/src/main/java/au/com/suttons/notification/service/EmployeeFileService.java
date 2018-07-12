package au.com.suttons.notification.service;


import au.com.suttons.notification.bean.EmployeeFileDetailBean;
import au.com.suttons.notification.data.dao.EmployeeDao;
import au.com.suttons.notification.data.dao.EmployeeFileDao;
import au.com.suttons.notification.data.dao.EmployeeFileDetailDao;
import au.com.suttons.notification.data.entity.EmployeeEntity;
import au.com.suttons.notification.data.entity.EmployeeFileDetailEntity;
import au.com.suttons.notification.data.entity.EmployeeFileEntity;
import au.com.suttons.notification.fileprocessor.EmployeeTerminationFileProcessor;
import au.com.suttons.notification.jobs.JobConstants;
import au.com.suttons.notification.util.DateUtil;
import au.com.suttons.notification.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.nio.file.Path;
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

    public void readEmployeeFiles() throws IOException {

        // Step 1 : Read all files from documents folder
        List<Path> files = FileUtil.getFilesInDirectory(
                FileUtil.getEmployeeFileLocation());

        for (Path file : files) {

            String fileName = file.getFileName().toString();
            String fileUrl = file.toString();

            // Step 2 : Check if file name already exists in the Employee File table
            EmployeeFileEntity employeeFile = employeeFileDao.findByName(fileName);

            if (employeeFile == null) {

                // Step 3 : In case file is new, read file and insert details into Employee File table
                EmployeeTerminationFileProcessor processor = new EmployeeTerminationFileProcessor();
                List employeeFileDetails = processor.getList(fileUrl);

                if(!employeeFileDetails.isEmpty()) {

                    employeeFile = new EmployeeFileEntity();
                    employeeFile.setFileUrl(fileUrl);
                    employeeFile.setFileName(fileName);
                    employeeFile.setStatus(JobConstants.STATUS_PROCESSED);
                    employeeFile.setLastUpdatedBy(JobConstants.USER_SYSTEM);

                    employeeFile = employeeFileDao.saveAndFlush(employeeFile);

                    saveEmployeeFileDetails(employeeFileDetails, employeeFile);
                }

            }

        }

    }

    public void saveEmployeeFileDetails(List<EmployeeFileDetailBean> employeeFileDetailList, EmployeeFileEntity employeeFile) {

        for (EmployeeFileDetailBean employeeDetail : employeeFileDetailList) {

            EmployeeFileDetailEntity entity = new EmployeeFileDetailEntity();

            entity.setEmployeeNumber(employeeDetail.getEmployeeNumber());
            entity.setFirstName(employeeDetail.getFirstName());
            entity.setLastName(employeeDetail.getLastName());
            entity.setDescription(employeeDetail.getDescription());
            entity.setPosition(employeeDetail.getPosition());
            if (StringUtils.isNotBlank(employeeDetail.getTerminationDate())) {
                entity.setTerminationDate(DateUtil.parseStringToDate(employeeDetail.getTerminationDate()));
            }
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
        }

        employee.setEmployeeNumber(employeeFileDetail.getEmployeeNumber());
        employee.setFirstName(employeeFileDetail.getFirstName());
        employee.setLastName(employeeFileDetail.getLastName());
        employee.setDescription(employeeFileDetail.getDescription());
        employee.setPosition(employeeFileDetail.getPosition());
        employee.setTerminationDate(employeeFileDetail.getTerminationDate());
        employee.setStatus(JobConstants.STATUS_ACTIVE);

        employee.setLastUpdatedBy(employeeFileDetail.getLastUpdatedBy());

        this.employeeDao.saveAndFlush(employee);
    }

}
