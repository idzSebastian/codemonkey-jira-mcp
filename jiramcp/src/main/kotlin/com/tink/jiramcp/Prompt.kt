package com.tink.jiramcp

import org.springframework.stereotype.Component

@Component
class Prompt {

    fun getJiraResponseAnalysisPrompt(jiraJsonResponse: String, inputs: Map<String, String> = emptyMap()): String {
        val template = """
            Analyze the following Jira API response data and provide comprehensive insights:

            **Jira API Response Data:**
            ```json
            {{jira_json}}
            ```

            **Analysis Parameters:**
            - Analysis Period: {{analysis_period}}

            **Required Analysis for Jira Response:**

            1. **Issue Overview:**
               - Total issues found (from 'total' field)
               - Issues returned in this batch (startAt, maxResults)
               - Issue keys and IDs summary

             2. **GitHub Integration Analysis:**
                - For any GitHub links found in the Jira response (in descriptions, comments, or custom fields)
                - Use GitHub MCP to examine what changes were made in each linked repository/PR/commit
                - Summarize code changes, files modified, and impact for each GitHub reference
                - Correlate code changes with ticket completion status and timeline
 
            **Output Format:**
            - Highlight any concerning patterns
            - Format in markdown with clear sections
            - Keep it short

            Additional Context: {{additional_context}}
        """.trimIndent()


        val allInputs = inputs.toMutableMap()
        allInputs["jira_json"] = jiraJsonResponse

        return replacePlaceholders(template, allInputs)
    }

    fun getJiraDeploymentAnalysisPrompt(jiraJsonResponse: String, inputs: Map<String, String> = emptyMap()): String {
        val template = """
            Analyze the following Jira API response data and provide comprehensive deployment insights:

            **Jira API Response Data:**
            ```json
            {{jira_json}}
            ```

            **Analysis Parameters:**
            - Analysis Period: {{analysis_period}}
            - Team: {{team}}

            **Required Deployment Analysis:**

            1. **Deployment Tickets Overview:**
               - What tickets have been deployed (ticket keys, summaries, and status)
               - Total deployment count from this period

            2. **For Each Deployment Ticket, Extract:**
               - **Service Deployed**: Identify the service name from description/fields
               - **Clusters Deployed**: List all clusters from description, labels, or custom fields
               - **Deployment Date & Time**: Extract deployment timestamp
               - **Approver**: Who approved the deployment
               - **Buildkite Link**: Extract Buildkite URL from description or custom fields

            3. **GitHub Integration & Code Analysis:**
               - Extract GitHub commit hashes from Jira descriptions/custom fields
               - For each commit hash found, use GitHub MCP to:
                 - Get commit details and changes
                 - Generate GitHub comparison/diff links between commits for each cluster
                 - Analyze what files were modified and impact
                 - Summarize code changes (features, bug fixes, refactoring, etc.)
               - Correlate GitHub changes with deployment timeline

            4. **Deployment Summary Report:**
               - Service deployment frequency and patterns
               - Cluster deployment coverage
               - Code change impact assessment
               - Deployment velocity (time from commit to deployment)
               - Risk indicators or concerning patterns

            **Output Requirements:**
            - Format in clean markdown with clear sections
            - Include clickable links to:
              - GitHub commit comparisons/diffs
              - Buildkite deployment builds
              - Original Jira tickets
            - Use tables for structured data where appropriate
            - Highlight deployment status (✅ Success, ⚠️ Pending, ❌ Failed)
            - Keep analysis comprehensive but concise

            **GitHub MCP Integration Instructions:**
            - Use mcp_github_get_commit for each commit hash found
            - Use mcp_github_get_pull_request_diff if PR numbers are referenced
            - Generate GitHub comparison URLs in format: https://github.com/owner/repo/compare/commit1...commit2
            - Summarize actual code changes from GitHub API responses

            Additional Context: {{additional_context}}
        """.trimIndent()

        val allInputs = inputs.toMutableMap()
        allInputs["jira_json"] = jiraJsonResponse

        return replacePlaceholders(template, allInputs)
    }

    fun getJiraTicketSummaryPrompt(jiraJsonResponse: String, inputs: Map<String, String> = emptyMap()): String {
        val template = """
            Generate a concise summary from this Jira API response:

            **Data:**
            ```json
            {{jira_json}}
            ```

            **Quick Summary Focus:**
            - Total tickets: Extract from 'total' field
            - Open vs other statuses
            - Key assignees: {{focus_assignee}}
            - Date range: {{date_filter}}

            **Provide:**
            1. Executive summary (2-3 sentences)
            2. Key metrics table
            3. Top priorities or concerns
            4. Next actions recommended

            Keep it concise and actionable for: {{audience}}
        """.trimIndent()

        val allInputs = inputs.toMutableMap()
        allInputs["jira_json"] = jiraJsonResponse

        return replacePlaceholders(template, allInputs)
    }

    private fun replacePlaceholders(template: String, inputs: Map<String, String>): String {
        var result = template
        inputs.forEach { (key, value) ->
            result = result.replace("{{$key}}", value)
        }
        return result
    }
}