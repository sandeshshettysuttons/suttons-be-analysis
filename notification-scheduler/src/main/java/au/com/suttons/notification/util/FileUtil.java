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

	public static String getEmployeeFileLocation() {
		String subLocation = "/" + EMPLOYEE_FILE_FOLDER + "/";
		return AppConfig.networkFileStorageLocation + subLocation;
	}

	public static List<Path> getFilesInDirectory(String dirPath) throws IOException {

		Path path = Paths.get(dirPath);
		final List<Path> files = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (!Files.isDirectory(entry)) {
					files.add(entry);
				}
			}
		}

		return files;
	}

}
