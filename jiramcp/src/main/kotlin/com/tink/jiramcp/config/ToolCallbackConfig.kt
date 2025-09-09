package com.tink.jiramcp.config

import com.tink.jiramcp.JiraTool
import org.springframework.ai.support.ToolCallbacks
import org.springframework.ai.tool.ToolCallback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ToolCallbackConfig {

    @Bean
    fun bgrTools(jiraTool: JiraTool): List<ToolCallback> {
        return ToolCallbacks.from(jiraTool).toList()
    }


}