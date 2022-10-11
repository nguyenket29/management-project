package com.hau.ketnguyen.it.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public final class EntryPointAuthenticationConfig implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (httpServletResponse.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String msg = "Thông tin xác thực không hợp lệ";
            log.error("Unauthorized: {}", msg);
            mapper(httpServletResponse, httpServletRequest, e, "Unauthorized", msg);
        } else if (httpServletResponse.getStatus() == HttpServletResponse.SC_FORBIDDEN) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            String msg = "Path không hợp lệ";
            log.error("Forbidden: {}", msg);
            mapper(httpServletResponse, httpServletRequest, e, "Forbidden", msg);
        } else if (httpServletResponse.getStatus() == HttpServletResponse.SC_BAD_REQUEST) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String msg = "Bad request";
            log.error("Bad request: {}", msg);
            mapper(httpServletResponse, httpServletRequest, e, "Bad request", msg);
        } else if (httpServletResponse.getStatus() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String msg = "Internal server error";
            log.error("Internal server error: {}", msg);
            mapper(httpServletResponse, httpServletRequest, e, "Internal server error", msg);
        } else if (httpServletResponse.getStatus() == HttpServletResponse.SC_NOT_FOUND) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            String msg = "Not found";
            log.error("Not found: {}", msg);
            mapper(httpServletResponse, httpServletRequest, e, "Not found", msg);
        }
    }

    private void mapper(HttpServletResponse response, HttpServletRequest request,
                        AuthenticationException e, String error, String message) throws IOException {
        final Map<String, Object> body = new HashMap<>();
        body.put("status", response.getStatus());
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getServletPath());
        body.put("timestamp", Calendar.getInstance().getTime());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
