package edu.kh.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.kh.project.common.interceptor.BoardTypeInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{

	
	@Bean // BoardTypeInterceptor Bean 으로 등록하고
	public BoardTypeInterceptor boardTypeInterceptor() {
		return new BoardTypeInterceptor();
	}

	
	
	@Override //Bean 으로 등록된 인터셉터를 WebMvcConfigurer 에 등록
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor( boardTypeInterceptor() )
		.addPathPatterns("/**")  // 가로챌 경로 지정(여러개 작성 시 , 구분)
		.excludePathPatterns("/css/**", "/images/**", "/js/**"); //가로채지 않을 경로
		
		
		//인터셉터를 추가로 만들고 싶으면 Bean등록 후  여기에 추가로 경로 지정.
		// registry.addInterceptor( boardTypeInterceptor() )
	}
	
	
	
}
