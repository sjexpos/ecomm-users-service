package io.oigres.ecomm.service.users.config;

import java.util.List;
import java.util.stream.Stream;

import jakarta.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.web.PageableRequestHandlerMethodArgumentResolver;
import org.springframework.data.web.SortRequestHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.oigres.ecomm.service.users.web.http.LoggingRequestFilter;

import org.springframework.web.cors.CorsConfiguration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {
    
	@Bean
	public FilterRegistrationBean<LoggingRequestFilter> loggingRequestFilterRegistrationBean() {
		FilterRegistrationBean<LoggingRequestFilter> registrationBean = new FilterRegistrationBean<LoggingRequestFilter>();
		LoggingRequestFilter filter = new LoggingRequestFilter();
		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("*");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE-1);
		return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean<Filter> corsFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
		filterRegistrationBean.setFilter(new CorsFilter(request -> {
			String origin = request.getHeader(HttpHeaders.ORIGIN);
			log.debug("origin = {}", origin);
			if (!StringUtils.hasText(origin)) {
				return null;
			}
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.addAllowedOrigin(origin);
			String accessControlRequestHeaders = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
			if (StringUtils.hasText(accessControlRequestHeaders)) {
				Stream.of(accessControlRequestHeaders.split(",")).map(String::trim).distinct()
						.forEach(configuration::addAllowedHeader);
			}
			configuration.addExposedHeader("*");
			configuration.setAllowCredentials(true);
			configuration.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"));
			return configuration;
		}));
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setOrder(Integer.MIN_VALUE); // Ensure first execution
		return filterRegistrationBean;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(pageableRequestResolver());
		resolvers.add(sortRequestResolver());
	}

	public PageableRequestHandlerMethodArgumentResolver pageableRequestResolver() {
		return new PageableRequestHandlerMethodArgumentResolver();
	}

	public SortRequestHandlerMethodArgumentResolver sortRequestResolver() {
		return new SortRequestHandlerMethodArgumentResolver();
	}

}

