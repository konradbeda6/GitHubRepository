package com.example.Repositories.client;

import com.example.Repositories.exception.RepositoryNotFoundException;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = feign.FeignException.errorStatus(methodKey, response);

        if (response.status() == 404) {
            return new RepositoryNotFoundException();
        }

        if( response.status() == 503){
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    50L,
                    response.request());
        }
        return exception;
    }
}
