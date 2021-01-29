package com.mobiquity.sse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SseKotlinDemoApplication

fun main(args: Array<String>) {
	runApplication<SseKotlinDemoApplication>(*args)
}
