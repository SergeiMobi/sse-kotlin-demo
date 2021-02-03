package com.mobiquity.sse.service

import com.mobiquity.sse.model.EventModel
import org.springframework.stereotype.Service

@Service
class Publisher (private val pubSubService: PubSubService){

    fun publish(event: EventModel) {
        pubSubService.sendMessage(event)
    }
}