package io.oigres.ecomm.service.users.web.http;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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