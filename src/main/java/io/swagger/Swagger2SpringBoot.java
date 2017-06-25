/*
 * 
 */
package io.swagger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class Swagger2SpringBoot.
 * 
 * This is the Main class of this application.
 * 
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"io.swagger", "mapping"})
public class Swagger2SpringBoot implements CommandLineRunner {

	/* (non-Javadoc)
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... arg0) throws Exception {
		if (arg0.length > 0 && arg0[0].equals("exitcode")) {
			throw new ExitException();
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		new SpringApplication(Swagger2SpringBoot.class).run(args);
	}

	/**
	 * The Class ExitException.
	 */
	class ExitException extends RuntimeException implements ExitCodeGenerator {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see org.springframework.boot.ExitCodeGenerator#getExitCode()
		 */
		@Override
		public int getExitCode() {
			return 10;
		}

	}
}
