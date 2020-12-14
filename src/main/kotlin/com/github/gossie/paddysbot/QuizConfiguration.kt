package com.github.gossie.paddysbot;

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate

@Configuration
class QuizConfiguration {

    @Bean
    fun restTemplate() {
        return RestTemplate()
    }

}
