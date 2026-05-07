package com.example.Repositories.client;

import com.example.Repositories.exception.RepositoryNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 404) {
            return new RepositoryNotFoundException();
        }
        return new Exception("Generic error");
    }
}
