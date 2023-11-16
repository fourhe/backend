package com.letter2sea.be.common.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

@Configuration
//@EnableWebMvc //LocalDateTime response가 array 형식으로 반환되어 임시 주석처리
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
            .resolver(DefaultAddressResolverGroup.INSTANCE);
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }
}
