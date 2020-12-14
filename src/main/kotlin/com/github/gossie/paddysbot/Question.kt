package com.github.gossie.paddysbot;

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID;

data class Question(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("question") val question: String,
    @JsonProperty("correctAnswer") val correctAnswer: String?
)
