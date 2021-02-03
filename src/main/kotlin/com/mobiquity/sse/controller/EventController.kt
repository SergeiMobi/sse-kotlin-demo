package com.mobiquity.sse.controller

import com.mobiquity.sse.dto.EventDto
import com.mobiquity.sse.service.EventService
import com.mobiquity.sse.service.Subscriber
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.LocalTime
import java.util.concurrent.ExecutorService

@RestController
class EventController constructor(
        val eventService: EventService, val executorService: ExecutorService, val subscriber: Subscriber) {

    @GetMapping("/event")
    fun getEvent(): EventDto {
        return EventDto("test event")
    }

    @PostMapping("/event")
    fun addEvent(@RequestBody event: EventDto): ResponseEntity<Any> {
        eventService.addEvent(event)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/stream-sse-random-data")
    fun streamSseRandomData(): SseEmitter? {
        val emitter = SseEmitter()
        executorService.execute {
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
    fun streamSseTimeout(): SseEmitter? {
        return SseEmitter(15 * 1000)
    }

    @GetMapping("/stream-sse-mongodb-push")
    fun streamSseMongodbPush(): SseEmitter? {
        val emitter = SseEmitter() // 30 seconds by default
        executorService.execute {
            try {
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
            emitter.complete()
        }
        return emitter
    }
}