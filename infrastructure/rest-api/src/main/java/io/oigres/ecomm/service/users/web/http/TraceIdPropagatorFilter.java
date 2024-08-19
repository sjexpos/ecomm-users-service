package io.oigres.ecomm.service.users.web.http;

import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import io.oigres.ecomm.service.users.Constants;

public class TraceIdPropagatorFilter extends OncePerRequestFilter {
    
	public TraceIdPropagatorFilter() {
	}

	@Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws java.io.IOException, ServletException {
        try {
            final String token;
            if (request.getHeader(Constants.HTTP_HEADER_DISTRIBUTED_TRACE_ID) != null) {
            	token = request.getHeader(Constants.HTTP_HEADER_DISTRIBUTED_TRACE_ID);
            } else {
                token = UUID.randomUUID().toString().toUpperCase().replace("-", "");
            }
            MDC.put(Constants.HTTP_HEADER_DISTRIBUTED_TRACE_ID, token);
            response.addHeader(Constants.HTTP_HEADER_DISTRIBUTED_TRACE_ID, token);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(Constants.HTTP_HEADER_DISTRIBUTED_TRACE_ID);
        }
    }

}
