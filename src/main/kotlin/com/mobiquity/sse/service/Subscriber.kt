package com.mobiquity.sse.service

import com.mobiquity.sse.model.Event
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class Subscriber(pubSubService: PubSubService) {
    var subscribe: Consumer<Event>? = null

    init {
        pubSubService.registerSubscriber(this)
    }

    fun receivedMessage(event: Event) {
            subscribe?.accept(event)
    }

    fun subscribe(consumer: Consumer<Event>) {
        this.subscribe = consumer
    }
}