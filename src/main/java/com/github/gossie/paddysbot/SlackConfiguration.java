package com.github.gossie.paddysbot;

import com.slack.api.bolt.App;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfiguration {

    @Bean
    public App initSlackApp() {
        var app = new App();
        app.command("/hello", (req, ctx) -> ctx.ack("What's up?"));
        return app;
    }

}
