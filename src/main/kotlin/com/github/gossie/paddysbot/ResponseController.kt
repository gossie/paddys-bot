package com.github.gossie.paddysbot

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus

@Controller("/slack/response")
class ResponseController {

    @PostMapping("choice")
    @ResponseStatus(HttpStatus.OK)
    fun handleResponse(@RequestBody body: Map<String, Object>) {

    }

}