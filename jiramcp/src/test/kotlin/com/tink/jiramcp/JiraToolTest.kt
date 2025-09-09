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
        jiraTool.getUserTicketsFromJira(from = "2023-10-01", to = "2023-10-31", team = "data-refresh")

    }
}