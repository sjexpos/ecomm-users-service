/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.users.config;

import io.oigres.ecomm.service.users.web.http.LoggingRequestFilter;
import jakarta.servlet.Filter;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.web.PageableRequestHandlerMethodArgumentResolver;
import org.springframework.data.web.SortRequestHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {

  @Bean
  public FilterRegistrationBean<LoggingRequestFilter> loggingRequestFilterRegistrationBean() {
    FilterRegistrationBean<LoggingRequestFilter> registrationBean =
        new FilterRegistrationBean<LoggingRequestFilter>();
    LoggingRequestFilter filter = new LoggingRequestFilter();
    registrationBean.setFilter(filter);
    registrationBean.addUrlPatterns("*");
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE - 1);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<Filter> corsFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
    filterRegistrationBean.setFilter(
        new CorsFilter(
            request -> {
              String origin = request.getHeader(HttpHeaders.ORIGIN);
              log.debug("origin = {}", origin);
              if (!StringUtils.hasText(origin)) {
                return null;
              }
              CorsConfiguration configuration = new CorsConfiguration();
              configuration.addAllowedOrigin(origin);
              String accessControlRequestHeaders =
                  request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
              if (StringUtils.hasText(accessControlRequestHeaders)) {
                Stream.of(accessControlRequestHeaders.split(","))
                    .map(String::trim)
                    .distinct()
                    .forEach(configuration::addAllowedHeader);
              }
              configuration.addExposedHeader("*");
              configuration.setAllowCredentials(true);
              configuration.setAllowedMethods(
                  List.of("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"));
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
