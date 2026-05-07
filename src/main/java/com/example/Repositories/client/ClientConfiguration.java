package com.example.Repositories.client;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class ClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
