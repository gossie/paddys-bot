package com.github.gossie.paddysbot

import com.slack.api.app_backend.slash_commands.response.SlashCommandResponse
import com.slack.api.bolt.App
import com.slack.api.model.block.ActionsBlock
import com.slack.api.model.block.HeaderBlock
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.RichTextBlock
import com.slack.api.model.block.composition.DispatchActionConfig
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.element.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.regex.Pattern

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
                                .actionId("choice-${it.id}")
                                .value(it.choice)
                                //.url("/choice")
                                .build()
                        }

                    ctx.ack(
                        listOf(
                            HeaderBlock.builder().text(PlainTextObject.builder().text(question.question).build()).build(),
                            ActionsBlock.builder().elements(choiceElements).build()
                        )
                    )
                }
                else -> {
                    val input = listOf(PlainTextInputElement.builder()
                        .actionId("input")
                        .dispatchActionConfig(DispatchActionConfig.builder()
                            .triggerActionsOn(listOf("on_enter_pressed"))
                            .build())
                        .build())

                    ctx.ack(
                        listOf(
                            HeaderBlock.builder().text(PlainTextObject.builder().text(question.question).build()).build(),
                            ActionsBlock.builder().elements(input).build()
                        )
                    )
                }
            }
        }

        app.blockAction(Pattern.compile("choice-\\w+-\\w+-\\w+-\\w+-\\w+")) { req, ctx ->
            ctx.respond("Deine Antwort war ${req.payload.actions[0].value}")
            ctx.ack()
        }

        app.blockAction("input") { req, ctx ->
            ctx.respond("Deine Antwort war ${req.payload.actions[0].value}")
            ctx.ack()
        }

        return app;
    }

}
