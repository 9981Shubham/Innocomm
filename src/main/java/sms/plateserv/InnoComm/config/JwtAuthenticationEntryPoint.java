package sms.plateserv.InnoComm.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LogManager.getLogger(JwtAuthenticationEntryPoint.class);

	private final HandlerExceptionResolver resolver;

	@Autowired
	public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse httpServletResponse,
			AuthenticationException ex) throws IOException {
		logger.error("User is unauthorised. Routing from the entry point");
		if (request.getAttribute("javax.servlet.error.exception") != null) {
			Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
			logger.error("User is unauthorised with exception {}", (Exception)throwable);
			resolver.resolveException(request, httpServletResponse, null, (Exception) throwable);
		}
		if (!httpServletResponse.isCommitted()) {
			httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		}
	}
}
