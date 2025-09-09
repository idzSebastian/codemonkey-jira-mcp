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
            - Project Focus: {{project}}
            - Analysis Period: {{analysis_period}}
            - Team: {{team}}

            **Required Analysis for Jira Response:**

            1. **Issue Overview:**
               - Total issues found (from 'total' field)
               - Issues returned in this batch (startAt, maxResults)
               - Issue keys and IDs summary

            2. **Issue Status Analysis:**
               - Status distribution (Open, In Progress, Done, etc.)
               - Status categories breakdown
               - Issues by current state

            3. **Assignment Analysis:**
               - Who is assigned to what tickets
               - Workload distribution among team members
               - Unassigned tickets identification

            4. **Timeline Analysis:**
               - Issue creation dates and patterns
               - Last updated timestamps
               - Age of open issues (time since creation)

            5. **Work Items Breakdown:**
               - Issue summaries and types
               - Priority analysis (if available)
               - Related work patterns

            6. **Team Productivity Insights:**
               - Active contributors
               - Issue velocity and throughput
               - Potential bottlenecks

            **Output Format:**
            - Provide clear metrics and percentages
            - Highlight any concerning patterns
            - Include actionable recommendations
            - Format in markdown with clear sections

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