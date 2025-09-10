package com.tink.jiramcp

import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.annotation.Tool
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class JiraTool(private val webClient: WebClient, @Value("\${jira.encodedCredentials}") val encodedCredentials: String) {
    private val jiraDomain = "https://tinkab.atlassian.net"
    private val logger: Logger = LoggerFactory.getLogger(JiraTool::class.java)

    @Tool(name = "jira_team_last_deployed_changes",
        description = "Analyzes Jira tickets to provide insights on what has been deployed, when deployments occurred, and by which team members. Takes date range parameters (from/to) and returns a detailed prompt for analyzing deployment activities, ticket statuses, assignee workload, and team productivity metrics.")
    fun getUserTicketsFromJira(from: String, to: String, team: String): String {
        return runBlocking {
            val result = queryJson(from, to)
            val prompt = Prompt().getJiraResponseAnalysisPrompt(
                jiraJsonResponse = result,
                inputs = mapOf(
                    "project" to "DER",
                    "analysis_period" to "$from to $to",
                    "team" to team,
                    "additional_context" to "Focus on deployment activities and team productivity for the specified date range"
                )
            )
            prompt
        }
    }

    suspend fun queryJson(from: String, to: String): String {
        // Step 1: Search for issues
        val searchUrl = "$jiraDomain/rest/api/3/search/jql"
        val jqlBody = """{
                "jql": "project = DER AND status = DONE AND assignee in (6388d792489de2f7f466a6c6, 5f3107dc3aa35b003fc204f7, 712020:8a7d94a5-ed07-4fb2-bb4d-be9a96562376, 712020:b759c6ae-996d-4dde-881e-11a58fe882d6) AND  created >= $from AND created <= $to",
                "fields": [
                  "summary",
                  "description",
                  "status",
                  "details",
                  "labels",
                  "assignee"
                ]
            }
        """.trimIndent()
        val searchResult = webClient.post()
            .uri(searchUrl)
            .headers { headers ->
                headers.setBasicAuth(encodedCredentials)
                headers.contentType = MediaType.APPLICATION_JSON
            }
            .bodyValue(jqlBody)
            .retrieve()
            .awaitBody<Map<String, Any>>()

        logger.info("Search Result: $searchResult")
        return searchResult.toString()
    }

    suspend fun mockQueryJson(): String {
        return try {
            val inputStream = this::class.java.getResourceAsStream("/sample-response.json")
            inputStream?.bufferedReader()?.use { it.readText() } ?: ""
        } catch (e: Exception) {
            logger.error("Error reading mock data", e)
            ""
        }
    }
}