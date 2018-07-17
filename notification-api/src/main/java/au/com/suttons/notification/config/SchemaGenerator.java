package au.com.suttons.notification.config;

import javax.persistence.Persistence;

public class SchemaGenerator {
	public static void main(String[] args) {
		Persistence.generateSchema("notification", null);
	}
}
