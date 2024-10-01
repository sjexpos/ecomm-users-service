package io.oigres.ecomm.service.users.web.http;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingRequestFilter implements Filter {
	static private final List<Pattern> EXCLUDED_PATHS = List.of(
			Pattern.compile("/"),
			Pattern.compile("/favicon.ico"),
			Pattern.compile("/actuator"),
			Pattern.compile("/actuator/.*"),
			Pattern.compile("/swagger"),
			Pattern.compile("/swagger/.*"),
			Pattern.compile("/swagger-ui"),
			Pattern.compile("/swagger-ui/.*"),
			Pattern.compile("/api/swagger-config")
	);

	private final MemoryMXBean memoryBean;

	public LoggingRequestFilter() {
		this.memoryBean = ManagementFactory.getMemoryMXBean();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
            log.info(String.format("END - %s %s - status: %s - time: %dms - heap: %s", method, requestUri, httpResponse.getStatus(), (end-start), FileUtils.byteCountToDisplaySize(endHeap-startHeap) ));
        }
	}

}