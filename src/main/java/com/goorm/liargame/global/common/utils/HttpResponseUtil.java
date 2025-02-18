package com.goorm.liargame.global.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.liargame.global.exception.BaseErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HttpResponseUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

//	public static void sendJsonResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
//		throws IOException {
//		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//		response.setStatus(httpStatus.value());
//		response.setCharacterEncoding("UTF-8");
//		String responseBody = objectMapper.writeValueAsString(body);
//		response.getWriter().write(responseBody);
//	}

	public static void sendJsonResponse(HttpServletResponse response, BaseErrorCode errorCode)
			throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(errorCode.getStatus().value());
		response.setCharacterEncoding("UTF-8");
		String responseBody = objectMapper.writeValueAsString(errorCode.toApiResponse());
		response.getWriter().write(responseBody);
	}
}
