package com.github.gossie.paddysbot

import com.slack.api.app_backend.slash_commands.response.SlashCommandResponse
import com.slack.api.bolt.App
import com.slack.api.model.block.ActionsBlock
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.BlockElement
import com.slack.api.model.block.element.BlockElements
import com.slack.api.model.block.element.ButtonElement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SlackConfiguration {

    @Bean
    fun initSlackApp(questionLoader: QuestionLoader): App {
        val app = App()
        app.command("/echo") { req, ctx -> ctx.ack(req.payload.text) }
        app.command("/question") { _, ctx ->
            val question = questionLoader.determineRandomQuestion()

            when {
                question.choices != null -> {
                    val choiceElements = question.choices
                        .map {
                            ButtonElement.builder()
                                .text(PlainTextObject.builder()
                                    .text(it.choice)
                                    .build())
                                .build()
                        }

                    ctx.ack(listOf(ActionsBlock.builder()
                        .elements(choiceElements)
                        .build()))
                }
                else -> ctx.ack(question.question)
            }
        }

        return app;
    }

}
