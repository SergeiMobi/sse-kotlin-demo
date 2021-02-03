package com.mobiquity.sse.controller

import com.mobiquity.sse.dto.EventDto
import com.mobiquity.sse.service.EventService
import com.mobiquity.sse.service.pubsub.Subscriber
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.LocalTime

@RestController
class EventController constructor(
        val eventService: EventService, val threadPoolTaskExecutor: ThreadPoolTaskExecutor, val subscriber: Subscriber) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/event")
    fun addEvent(@RequestBody event: EventDto): ResponseEntity<Any> {
        eventService.addEvent(event)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/stream-sse-random-data")
    fun streamSseRandomData(): SseEmitter {
        val emitter = SseEmitter()
        threadPoolTaskExecutor.execute {
            try {
                var i = 0
                while (true) {
                    val event = SseEmitter.event()
                            .data("SSE MVC - " + LocalTime.now().toString())
                            .id(i.toString()) // optional parameter
                            .name("sse event - mvc") // optional parameter
                    emitter.send(event)
                    Thread.sleep(1000)
                    i++
                }
            } catch (ex: Exception) {
                emitter.completeWithError(ex)
            }
        }
        return emitter
    }

    @GetMapping("/stream-sse-timeout")
    fun streamSseTimeout(): SseEmitter {
        return SseEmitter(15 * 1000)
    }

    @GetMapping("/stream-sse-mongodb-push")
    fun streamSseMongodbPush(): SseEmitter {
        val emitter = SseEmitter() // 30 seconds by default
        threadPoolTaskExecutor.execute {
            try {
                log.debug("Subscribe to mongodb events")
                var pushEventNumber = 3
                while (pushEventNumber > 0) {
                    subscriber.subscribe { pushEvent ->
                        val sseEntry = SseEmitter
                                .event()
                                .data(pushEvent.toString())
                        emitter.send(sseEntry)
                        pushEventNumber--
                    }
                }
            } catch (ex: Exception) {
                emitter.completeWithError(ex)
            }
            subscriber.unsubscribe()
            log.debug("Unsubscribe from mongodb events")
            emitter.complete()
            log.debug("Emitter complete")
        }
        return emitter
    }
}