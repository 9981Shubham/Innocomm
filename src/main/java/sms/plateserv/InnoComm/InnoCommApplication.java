package sms.plateserv.InnoComm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InnoCommApplication {

	private static final Logger logger = LogManager.getLogger(InnoCommApplication.class);

	public static void main(String[] args) {
		logger.info("Application started");
		SpringApplication.run(InnoCommApplication.class, args);
		// swagger url : http://localhost:8092/swagger-ui.html#!
	}
}
