package com.github.gossie.paddysbot

import com.slack.api.bolt.App
import com.slack.api.bolt.servlet.SlackAppServlet

import javax.servlet.annotation.WebServlet

@WebServlet("/slack/events")
class PaddysBotController(app: App) : SlackAppServlet(app)
