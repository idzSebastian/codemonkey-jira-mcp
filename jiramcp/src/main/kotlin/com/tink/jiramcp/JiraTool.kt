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
class JiraTool(private val webClient: WebClient, @Value("\${jira.token}") val apiToken: String) {
    private val jiraDomain = "https://tinkab.atlassian.net"
    private val email = "sidzkows@visa.com"
    private val logger: Logger = LoggerFactory.getLogger(JiraTool::class.java)

    @Tool(name = "jira_team_last_deployed_changes",
        description = "Analyzes Jira tickets to provide insights on what has been deployed, when deployments occurred, and by which team members. Takes date range parameters (from/to) and returns a detailed prompt for analyzing deployment activities, ticket statuses, assignee workload, and team productivity metrics.")
    fun getUserTicketsFromJira(from: String, to: String, team: String): String {
        return runBlocking {
            val result = mockQueryJson()
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
//            queryJson(from, to)
        }
    }


    suspend fun queryJson(from: String, to: String): String {
        // Step 1: Search for issues
        val searchUrl = "$jiraDomain/rest/api/3/search"
        val jqlQuery = mapOf(
            "jql" to """""project = DER AND assignee = "sidzkows@visa.com"""",
            "fields" to listOf("key"),
            "maxResults" to 50
        )
        val searchResult = webClient.post()
            .uri(searchUrl)
            .headers { headers ->
                headers.setBasicAuth(email, apiToken)
                headers.contentType = MediaType.APPLICATION_JSON
            }
            .bodyValue(jqlQuery)
            .retrieve()
            .awaitBody<Map<String, Any>>()

        logger.info("Search Result: $searchResult")
        return ""
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