package com.example.jobMaintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class JobMaintenanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobMaintenanceApplication.class, args);
	}

}
