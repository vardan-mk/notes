package am.vardanmk.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
@ComponentScan(basePackages = "am.vardanmk.etl")
public class EtlApplication {

	@Bean
	@Profile("prod")
	public HandlerMapping handlerMapping() {
		return new RequestMappingHandlerMapping();
	}

	@Bean
	@Profile("prod")
	public HandlerAdapter handlerAdapter() {
		return new RequestMappingHandlerAdapter();
	}

//	@Bean
//	@Profile("!dev")
//	public HandlerExceptionResolver handlerExceptionResolver() {
//		return new HandlerExceptionResolver() {
//			@Override
//			public ResponseEntity<BatchStatus> resolveException(HttpServletRequest request,
//															   HttpServletResponse response,
//															   Object handler, Exception ex) {
//				return null;
//			}
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(EtlApplication.class, args);
	}

}
