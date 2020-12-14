package com.github.gossie.paddysbot;

import com.slack.api.bolt.App;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SlackConfiguration {

    @Bean
    fun initSlackApp(questionLoader: QuestionLoader): App {
        val app = App();
        app.command("/echo") { req, ctx -> ctx.ack(req.payload.text) };
        app.command("/question") { _, ctx ->
            val question = questionLoader.determineRandomQuestion()
            ctx.ack(question.question);
        }
        return app;
    }

}
