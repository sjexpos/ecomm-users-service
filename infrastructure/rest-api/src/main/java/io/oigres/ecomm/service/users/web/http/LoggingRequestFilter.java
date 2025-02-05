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

package io.oigres.ecomm.service.users.web.http;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class LoggingRequestFilter implements Filter {
  private static final List<Pattern> EXCLUDED_PATHS =
      List.of(
          Pattern.compile("/"),
          Pattern.compile("/favicon.ico"),
          Pattern.compile("/actuator"),
          Pattern.compile("/actuator/.*"),
          Pattern.compile("/swagger"),
          Pattern.compile("/swagger/.*"),
          Pattern.compile("/swagger-ui"),
          Pattern.compile("/swagger-ui/.*"),
          Pattern.compile("/api/swagger-config"));

  private final MemoryMXBean memoryBean;

  public LoggingRequestFilter() {
    this.memoryBean = ManagementFactory.getMemoryMXBean();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String method = httpRequest.getMethod() != null ? httpRequest.getMethod() : "";
    String requestUri = httpRequest.getRequestURI() != null ? httpRequest.getRequestURI() : "";
    String queryString = httpRequest.getQueryString() != null ? httpRequest.getQueryString() : "";
    if (EXCLUDED_PATHS.stream().anyMatch(p -> p.matcher(requestUri).matches())) {
      chain.doFilter(request, response);
      return;
    }
    long start = System.currentTimeMillis();
    long startHeap = memoryBean.getHeapMemoryUsage().getUsed();
    log.info(String.format("BEGIN - %s %s %s", method, requestUri, queryString));
    try {
      chain.doFilter(request, response);
    } finally {
      long end = System.currentTimeMillis();
      long endHeap = memoryBean.getHeapMemoryUsage().getUsed();
      log.info(
          String.format(
              "END - %s %s - status: %s - time: %dms - heap: %s",
              method,
              requestUri,
              httpResponse.getStatus(),
              (end - start),
              FileUtils.byteCountToDisplaySize(endHeap - startHeap)));
    }
  }
}
