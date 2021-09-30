package com.example.MyBookShopApp.utils;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class TestRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRequestInterceptor.class);

    private static final String UTF_8 = "UTF-8";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) {
        LOGGER.info("=== request begin ===");
        LOGGER.info("URI         : {}", request.getURI());
        LOGGER.info("Method      : {}", request.getMethod());
        LOGGER.info("Headers     : {}", request.getHeaders());
        LOGGER.info("Request body: {}", body, Charset.forName(UTF_8));
        LOGGER.info("===  request end  ===");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        LOGGER.info("=== response begin ===");
        LOGGER.info("Status code  : {}", response.getStatusCode());
        LOGGER.info("Status text  : {}", response.getStatusText());
        LOGGER.info("Headers      : {}", response.getHeaders());
        LOGGER.info("Response body: {}", inputStringBuilder.toString());
        LOGGER.info("===  response end  ===");
    }

    public static String getTestUser(String token, String secretKey){

        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(Charset.forName(UTF_8)))
//                .setSigningKey(SecurityConstants.SECRET.getBytes(Charset.forName(UTF_8)))
                .parseClaimsJws(token.replace("token", ""))
                .getBody()
                .getSubject();
    }

}
