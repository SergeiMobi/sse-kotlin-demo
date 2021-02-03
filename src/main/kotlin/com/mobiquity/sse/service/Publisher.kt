package com.mobiquity.sse.service

import com.mobiquity.sse.model.Event
import org.springframework.stereotype.Service

@Service
class Publisher (private val pubSubService: PubSubService){

    fun publish(event: Event) {
        pubSubService.sendMessage(event)
    }
}