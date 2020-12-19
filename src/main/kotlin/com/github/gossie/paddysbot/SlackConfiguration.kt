package com.github.gossie.paddysbot

import com.slack.api.bolt.App
import com.slack.api.bolt.response.Response
import com.slack.api.bolt.util.JsonOps
import com.slack.api.model.ModelConfigurator
import com.slack.api.model.block.*
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.composition.BlockCompositions.option
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.block.element.*
import com.slack.api.model.block.element.BlockElements.plainTextInput
import com.slack.api.model.block.element.BlockElements.staticSelect
import com.slack.api.model.view.Views.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.slack.api.model.block.element.BlockElements.conversationsSelect

import com.slack.api.model.block.Blocks.section
import com.slack.api.model.block.SectionBlock.SectionBlockBuilder
import com.slack.api.model.block.element.ConversationsSelectElement.ConversationsSelectElementBuilder


internal class PrivateMetadata {
    var responseUrl: String? = null
    var commandArgument: String? = null
}

@Configuration
class SlackConfiguration {

    private val logger = LoggerFactory.getLogger(SlackConfiguration::class.java)

    @Bean
    fun initSlackApp(questionLoader: QuestionLoader): App {
        val app = App()
        app.command("/echo") { req, ctx -> ctx.ack(req.payload.text) }
        app.command("/question") { req, ctx ->
            val question = questionLoader.determineRandomQuestion()

            val elements = when {
                question.choices != null -> {
                    input { input ->
                        input
                            .element(staticSelect {
                                it.options(
                                    question.choices
                                        .map {
                                            option { oo -> oo.text(plainText(it.choice)).value(it.choice) }
                                        }
                                )
                            })
                            .label(plainText { pt -> pt.text("Deine Antwort") })

                    }
                }
                else -> {
                    input {
                        it
                            .element(plainTextInput { pti -> pti.actionId("input") })
                            .label(plainText { pt -> pt.text("Deine Antwort") })
                    }
                }
            }

            val data = PrivateMetadata()
            data.responseUrl = ctx.responseUrl
            data.commandArgument = req.getPayload().getText()

            logger.info("trigger id: ${ctx.triggerId}")
            val viewsOpenRes = ctx.client().viewsOpen { builder ->
                builder.triggerId(ctx.triggerId)
                    .view(
                        view { thisView ->
                            thisView.callbackId("question")
                                .type("modal")
                                .title(viewTitle { it.type("plain_text").text("Deine Frage").emoji(true) })
                                .submit(viewSubmit { it.type("plain_text").text("Submit").emoji(true) })
                                .close(viewClose { it.type("plain_text").text("Cancel").emoji(true) })
                                .privateMetadata(JsonOps.toJsonString(data))
                                .blocks(
                                    listOf(
                                        section { s: SectionBlockBuilder ->
                                            s
                                                .text(plainText("The channel we'll post the result"))
                                                .accessory(conversationsSelect { conv ->
                                                    conv
                                                        .actionId("notification_conv_id")
                                                        .responseUrlEnabled(true)
                                                        .defaultToCurrentConversation(true)
                                                })
                                        },
                                        header { it.text(plainText { it.text(question.question) }) },
                                        elements
                                    )
                                )
                        }
                    )
            }

            logger.info("viewsOpenRes: $viewsOpenRes")

            if (viewsOpenRes.isOk) {
                ctx.ack()
            } else {
                Response.builder().statusCode(500).body(viewsOpenRes.error).build()
            }
        }

        app.viewSubmission("question") { _, ctx ->
            logger.info("view submission came in")
            ctx.respond("Ist angekommen")
            ctx.ack()
        }

        return app
    }

}
