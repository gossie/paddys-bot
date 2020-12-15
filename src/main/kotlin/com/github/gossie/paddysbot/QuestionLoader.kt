package com.github.gossie.paddysbot

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class QuestionLoader(private val restTemplate: RestTemplate,
                     @Value("\${question.service.url}") private val url: String) {

    private val logger = LoggerFactory.getLogger(QuestionLoader::class.java)
    private val categories = listOf("other", "history", "science", "politics", "geography", "literature", "music", "movies", "sport");
    private var allQuestions: List<Question> = loadAllQuestions()

    private fun loadAllQuestions() = categories
        .flatMap { loadQuestions(it) }
        .filter { it.correctAnswer != null }

    @Scheduled(cron = "0 59 23 * * *")
    fun loadQuestionsScheduled() {
        allQuestions = loadAllQuestions()
    }

    fun loadQuestions(category: String): List<Question> {
        return restTemplate.getForEntity(this.url + "?category=" + category, Array<Question>::class.java)
            .body
            ?.toList() ?: emptyList()
    }

    fun determineRandomQuestion(): Question {
        logger.info("seleting ong ot ${allQuestions.size} questions")
        val randomIndex = (Math.random() * (allQuestions.size - 1)).toInt()
        return allQuestions[randomIndex]
    }

}
