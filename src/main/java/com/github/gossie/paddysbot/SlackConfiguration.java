package com.github.gossie.paddysbot;

import com.slack.api.bolt.App;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfiguration {

    @Bean
    public App initSlackApp() {
        var app = new App();
        app.command("/echo", (req, ctx) -> ctx.ack(req.getPayload().getText()));
        app.command("/question", (req, ctx) -> {
            ctx.respond(":wave: Currently I'm being implemented :nerd_face:");
            return ctx.ack();
        });
        return app;
    }

}
