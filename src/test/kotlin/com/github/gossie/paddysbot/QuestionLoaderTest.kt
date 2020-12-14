package com.github.gossie.paddysbot

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.web.client.RestTemplate
import java.util.*

internal class QuestionLoaderTest {

    @Test
    fun shouldLoadQuestions() {
        val question1 = Question(UUID.randomUUID(), "Warum?", "Darum!")
        val question2 = Question(UUID.randomUUID(), "Wieso?", "DARUM!")

        val restTemplate = mock(RestTemplate::class.java, RETURNS_DEEP_STUBS);
        `when`(restTemplate.getForEntity("https://someUrl?category=history", Array<Question>::class.java).body)
                .thenReturn(arrayOf(question1, question2))

        val questionLoader = QuestionLoader(restTemplate, "https://someUrl")

        assertThat(questionLoader.loadQuestions("history")).containsExactly(question1, question2)
    }

    @Test
    fun shouldDetermineRandomQuestion() {
        val restTemplate = mock(RestTemplate::class.java, RETURNS_DEEP_STUBS);
        `when`(restTemplate.getForEntity("https://someUrl?category=other", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))
        `when`(restTemplate.getForEntity("https://someUrl?category=history", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))
        `when`(restTemplate.getForEntity("https://someUrl?category=science", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))
        `when`(restTemplate.getForEntity("https://someUrl?category=politics", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))
        `when`(restTemplate.getForEntity("https://someUrl?category=geography", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))
        `when`(restTemplate.getForEntity("https://someUrl?category=literature", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))
        `when`(restTemplate.getForEntity("https://someUrl?category=music", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))
        `when`(restTemplate.getForEntity("https://someUrl?category=movies", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))
        `when`(restTemplate.getForEntity("https://someUrl?category=sport", Array<Question>::class.java).body)
            .thenReturn(arrayOf(Question(UUID.randomUUID(), "Warum?", "Darum!"), Question(UUID.randomUUID(), "Wieso?", "DARUM!")))

        val questionLoader = QuestionLoader(restTemplate, "https://someUrl")

        assertThat(questionLoader.determineRandomQuestion()).isNotNull
    }

}