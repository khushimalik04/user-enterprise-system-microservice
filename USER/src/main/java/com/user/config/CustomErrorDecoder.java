package com.user.config;


import com.commonlib.exception.CustomException;
import com.commonlib.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Converts Feign error payloads back into the shared exception model used by this service.
 */
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        try(InputStream is = response.body().asInputStream()){
            ErrorResponse errorResponse = objectMapper.readValue(is, ErrorResponse.class);
            return new CustomException(errorResponse.getMessage(), errorResponse.getStatus());
        } catch (IOException e){
            // Fall back to a generic exception when the downstream payload is missing or malformed.
            throw new CustomException("INTERNAL_SERVER_ERROR");
        }
    }

}
