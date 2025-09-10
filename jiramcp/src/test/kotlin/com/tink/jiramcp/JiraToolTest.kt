package com.tink.jiramcp


import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JiraToolTest {

    @Autowired
    private lateinit var jiraTool: JiraTool

    @Test
    fun getUserTicketsFromJira() {
        jiraTool.getUserTicketsFromJira(from = "2025-08-01", to = "2025-09-10", team = "data-refresh")

    }
}