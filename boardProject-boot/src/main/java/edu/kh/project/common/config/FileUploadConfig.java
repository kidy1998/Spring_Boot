package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;



@Configuration
@PropertySource("classpath:/config.properties")
public class FileUploadConfig implements WebMvcConfigurer{
	//WebMvcConfigurer
	//Spring에서 웹 관련 요청/응답 모든 설정들을 할 수 있는 모든 메서드를 제공해주는 인터페이스
	
	
	// 파일을 저장하기 전 임시로 저장할 메모리 용량
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long fileSizeThreshold;
	
	// 파일 1개당 크기제한
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize;
	
	//요청당 파일 크기제한
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize;
	
	@Bean //수동으로 Bean 생성하면 Spring 에서 관리
	public MultipartConfigElement configElement( ) {
		
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setFileSizeThreshold(DataSize.ofBytes(fileSizeThreshold));
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		
		return factory.createMultipartConfig();
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		// multipartResolver : 파일은 파일로, 텍스트는 텍스트로 자동 구분
		return new StandardServletMultipartResolver();
	}
	
	
	// web 에서 사용하는 자원(css,js,파일 등)을 다루는 방법을 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		String webPath = "/images/**"; // /images/ 로 시작되는 모든 요청 받음
		String resources = "file:///C:/uploadImages/"; // 실제로 자원이 저장되어있는 로컬 경로
		
		registry.addResourceHandler(webPath).addResourceLocations(resources);
	}
	
	
	
	
}
