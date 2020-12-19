package com.github.gossie.paddysbot

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class Question(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("question") val question: String,
    @JsonProperty("correctAnswer") val correctAnswer: String?,
    @JsonProperty("choices") val choices: List<Choice>? = null
)

data class Choice(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("choice") val choice: String
)