package com.goorm.liargame.auth.jwt.filter;


import com.goorm.liargame.auth.exception.CustomJwtException;
import com.goorm.liargame.auth.exception.JwtErrorCode;
import com.goorm.liargame.global.common.utils.HttpResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (CustomJwtException e) {
			log.warn("** Custom Jwt Exception **", e);

			HttpResponseUtil.sendJsonResponse(response, e.getErrorCode());
		} catch (Exception e) {
			log.error("** Jwt Exception **", e);

			HttpResponseUtil.sendJsonResponse(response, JwtErrorCode.JWT_ERROR);
		}
	}
}
