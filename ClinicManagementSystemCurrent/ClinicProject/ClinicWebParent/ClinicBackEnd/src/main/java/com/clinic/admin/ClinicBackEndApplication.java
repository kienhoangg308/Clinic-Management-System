package com.clinic.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EntityScan({"com.clinic.common.entity", "com.clinic.admin.user"})
public class ClinicBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicBackEndApplication.class, args);
	}

//	@Bean
//	public AuditorAware<String> auditorProvider() {
//		return new AuditorAwareImpl();
//	}

}
