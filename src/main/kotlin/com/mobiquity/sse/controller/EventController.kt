package com.mobiquity.sse.controller

import com.mobiquity.sse.dto.EventDto
import com.mobiquity.sse.service.EventService
import com.mobiquity.sse.service.Subscriber
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.LocalTime
import java.util.concurrent.Executors


@RestController
class EventController constructor(
        val eventService: EventService, val subscriber: Subscriber) {

    @GetMapping("/event")
    fun getEvent(): EventDto {
        return EventDto("test event")
    }

    @PostMapping("/event")
    fun addEvent(): ResponseEntity<Any> {
        eventService.addEvent(EventDto("NewEvent"))
        return ResponseEntity.ok().build()
    }
    @GetMapping("/stream-sse-random-data")
    fun streamSseRandomData(): SseEmitter? {
        val emitter = SseEmitter()
        val sseMvcExecutor = Executors.newSingleThreadExecutor()
        sseMvcExecutor.execute {
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

    @GetMapping("/stream-sse-mvc")
    fun streamSseMvc(): SseEmitter? {
        val emitter = SseEmitter()
        val sseMvcExecutor = Executors.newSingleThreadExecutor()
        sseMvcExecutor.execute {
            try {
                val isEventSent = false
                while (!isEventSent) {
                    subscriber.subscribe { e ->
                        val event = SseEmitter.event()
                            .data(e.toString())
//                            .id(i.toString()) // optional parameter
//                            .name("sse event - mvc") // optional parameter
                        emitter.send(event)
                    }
                }
            } catch (ex: Exception) {
                emitter.completeWithError(ex)
            }
        }
        return emitter
    }
}