package org.libex.additions.rest.logging.spring;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
public class LoggingInterceptor extends HandlerInterceptorAdapter {

	public static final String IDENTIFIER_KEY = "TrackingId";

	private static final AtomicLong identifier = new AtomicLong(0);

	private BiFunction<HttpServletRequest, HttpServletResponse, String> preMessage =
			(req, res) -> String.format("Starting %s", req.getRequestURL().toString());

	private BiFunction<HttpServletRequest, HttpServletResponse, String> postMessage =
			(req, res) -> String.format("Completed %s", req.getRequestURL().toString());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		MDC.put(IDENTIFIER_KEY, Long.toString(identifier.incrementAndGet()));

		log(request, response, preMessage);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		log(request, response, postMessage);

		int status = response.getStatus();
		if (status < 400) {
			log.debug("Result is {} for {}", status, request.getRequestURL());
		}
		else if (status < 500) {
			log.info("Result is {} for {}", status, request.getRequestURL());
		}
		else {
			log.error("Result is {} for {}", status, request.getRequestURL());
		}

		MDC.remove(IDENTIFIER_KEY);
	}

	private void log(
			HttpServletRequest request,
			HttpServletResponse response,
			BiFunction<HttpServletRequest, HttpServletResponse, String> messageFunction) {
		if (log.isInfoEnabled()) {
			String message = messageFunction.apply(request, response);
			if (!Strings.isNullOrEmpty(message)) {
				log.info(message);
			}
		}
	}
}
