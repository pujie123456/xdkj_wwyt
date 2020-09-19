package com.jib.wwyt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = {"com.jib.wwyt.mapper"})
@EnableCaching
@EnableAsync
@EnableScheduling
public class WWYTSpringBootApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {
		SpringApplication.run(WWYTSpringBootApplication.class, args);
	}
//	@Scheduled(cron = "0/5 * * * * *")
//	public void cron() {
//		System.out.println(new Date());
//	}

}
