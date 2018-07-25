package au.com.suttons.notification.util;

import au.com.suttons.notification.config.AppConfig;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static final String EMPLOYEE_FILE_FOLDER = "employeeFiles";
	public static final String IMPORTED_FILE_FOLDER = "imported";
	public static final String ERROR_FILE_FOLDER = "error";

	public static String getEmployeeFileLocation() {
		String subLocation = "/" + EMPLOYEE_FILE_FOLDER + "/";
		return AppConfig.networkFileStorageLocation + subLocation;
	}

	public static String getImportedEmployeeFilesLocation() {
		String subLocation = "/" + EMPLOYEE_FILE_FOLDER + "/" + IMPORTED_FILE_FOLDER + "/" ;
		return AppConfig.networkFileStorageLocation + subLocation;
	}

	public static String getErrorEmployeeFilesLocation() {
		String subLocation = "/" + EMPLOYEE_FILE_FOLDER + "/" + ERROR_FILE_FOLDER + "/" ;
		return AppConfig.networkFileStorageLocation + subLocation;
	}

	public static List<Path> getCSVFilesInDirectory(String dirPath) throws IOException {

		Path path = Paths.get(dirPath);
		final List<Path> files = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.csv")) {
			for (Path entry : stream) {
				if (!Files.isDirectory(entry)) {
					files.add(entry);
				}
			}
		}

		return files;
	}

}
