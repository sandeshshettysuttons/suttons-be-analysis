package au.com.suttons.notification.util;

import java.util.Date;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.resource.error.RestApiException;

public class FileUtil {

	public static String getLocation(String departmentCode, String fileName) {
		return getLocation(departmentCode, DateUtil.getCurrentDate(), fileName);
	}

	public static String getNetworkLocation(String departmentCode, String fileName) {
		return getNetworkLocation(departmentCode, DateUtil.getCurrentDate(), fileName);
	}

	public static String getLocation(String departmentCode, Date date, String fileName) {
		if(departmentCode == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null departmentCode");
		}
		if(date == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null date");
		}
		if(fileName == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null fileName");
		}

		String subLocation = "/"+departmentCode+"/"+DateUtil.stringYYYYmmDD(date)+"/";
		String destFileName = new Date().getTime()+"_"+fileName;

		return AppConfig.networkFileStorageLocation + subLocation + destFileName;
	}


	public static String getNetworkLocation(String departmentCode, Date date, String fileName) {
		if(departmentCode == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null departmentCode");
		}
		if(date == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null date");
		}
		if(fileName == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null fileName");
		}

		String subLocation = "/"+departmentCode+"/"+DateUtil.stringYYYYmmDD(date)+"/";
		String destFileName = new Date().getTime()+"_"+fileName;

		return AppConfig.networkFileStorageLocation + subLocation + destFileName;
	}

	public static String getTempLocation(String fileName){
		return AppConfig.networkFileStorageLocation + "/temp/" + fileName + "_" + DateUtil.getFileTimestamp();
	}

	public static String getUploadFileLocation(String departmentCode, String depotCode, String fileName) {
		if(departmentCode == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null departmentCode");
		}
		if(depotCode == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null depot code");
		}
		if(fileName == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null fileName");
		}

		String subLocation = "/UploadFiles/"+departmentCode+"/"+depotCode+"/"+DateUtil.auSize8Date(null)+"/";
		String destFileName = new Date().getTime()+"_"+fileName;

		return AppConfig.networkFileStorageLocation + subLocation + destFileName;
	}

	public static String getInvoicesNetworkLocation(Long departmentId, String fileName) {
		if(departmentId == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null departmentId");
		}

		String subLocation = "/Invoices/"+departmentId.longValue();

		String destFileName = "";
		if (fileName != null) {
			destFileName = "/" + fileName;
		}

		return AppConfig.networkFileStorageLocation + subLocation + destFileName;
	}

	public static String getDeliveryDocketNetworkLocation(String departmentCode, String fileName) {
		if(departmentCode == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null departmentCode");
		}
		if(fileName == null) {
			throw RestApiException.getBadRequestException("Unable to get file storage location: null fileName");
		}

		String subLocation = "/DeliveryDockets/"+departmentCode+"/";
		String destFileName = fileName;

		return AppConfig.networkFileStorageLocation + subLocation + destFileName;
	}


}
