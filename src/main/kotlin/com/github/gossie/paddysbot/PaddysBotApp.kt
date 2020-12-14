package com.github.gossie.paddysbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan
class PaddysBotApp {

}

fun main(args: Array<String>) {
    runApplication<PaddysBotApp>(*args)
}
