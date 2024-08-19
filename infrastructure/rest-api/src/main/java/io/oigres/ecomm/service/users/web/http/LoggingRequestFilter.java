package io.oigres.ecomm.service.users.web.http;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

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
	private MemoryMXBean memoryBean;

	public LoggingRequestFilter() {
		this.memoryBean = ManagementFactory.getMemoryMXBean();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestUri = httpRequest.getRequestURI();
		if (requestUri.contains("swagger")) {
			chain.doFilter(request, response);
			return;
		}
		long start = System.currentTimeMillis();
		long startHeap = memoryBean.getHeapMemoryUsage().getUsed();
		log.info(String.format("BEGIN REQUEST %s %s %s", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString()));
        try {
            chain.doFilter(request, response);
        } finally {
            long end = System.currentTimeMillis();
			long endHeap = memoryBean.getHeapMemoryUsage().getUsed();
            log.info(String.format("END REQUEST %s %s - status: %s - time: %dms - heap: %s", httpRequest.getMethod(), httpRequest.getRequestURI(), httpResponse.getStatus(), (end-start), FileUtils.byteCountToDisplaySize(endHeap-startHeap) ));
        }
	}

}