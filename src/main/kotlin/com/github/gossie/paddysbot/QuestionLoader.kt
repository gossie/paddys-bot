package com.github.gossie.paddysbot

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.concurrent.locks.ReentrantReadWriteLock

@Component
class QuestionLoader(private val restTemplate: RestTemplate,
                     @Value("\${question.service.url}") private val url: String) {

    private val logger = LoggerFactory.getLogger(QuestionLoader::class.java)
    private val categories = listOf("other", "history", "science", "politics", "geography", "literature", "music", "movies", "sport");
    private val lock = ReentrantReadWriteLock()
    private val allQuestions: MutableList<Question> = ArrayList(loadAllQuestions())

    private fun loadAllQuestions() = categories
        .flatMap { loadQuestions(it) }
        .filter { it.correctAnswer != null }
        .filter { it.question != "Was besagt das \"Mooresche Gesetz\"?"} // TODO: Länge der Antwort muss gehandled werden

    @Scheduled(cron = "0 59 23 * * *")
    fun loadQuestionsScheduled() {
        try {
            lock.writeLock().lock()
            allQuestions.clear()
            allQuestions.addAll(loadAllQuestions())
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun loadQuestions(category: String): List<Question> {
        return restTemplate.getForEntity(this.url + "?category=" + category, Array<Question>::class.java)
            .body
            ?.toList() ?: emptyList()
    }

    fun determineRandomQuestion(): Question {
        try {
            lock.readLock().lock()
            logger.info("selecting from ${allQuestions.size} questions")
            val randomIndex = (Math.random() * (allQuestions.size - 1)).toInt()
            return allQuestions[randomIndex]
        } finally {
            lock.readLock().unlock()
        }
    }

}
