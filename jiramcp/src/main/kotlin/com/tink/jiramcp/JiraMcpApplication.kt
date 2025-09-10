package com.tink.jiramcp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JiraMcpApplication

fun main(args: Array<String>) {
	runApplication<JiraMcpApplication>(*args)
}