package com.tink.jiramcp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class JiraWebClientConfig {

    @Bean
    fun jiraWebClient(): WebClient {
        // Configure and return your Jira WebClient here
        return WebClient.builder()
            .baseUrl("https://tinkab.atlassian.net")
            .build()
    }
}