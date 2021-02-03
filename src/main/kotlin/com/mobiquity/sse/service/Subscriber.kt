package com.mobiquity.sse.service

import com.mobiquity.sse.model.EventModel
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class Subscriber(pubSubService: PubSubService) {
    var subscribe: Consumer<EventModel>? = null

    init {
        pubSubService.registerSubscriber(this)
    }

    fun receivedMessage(pushEvent: EventModel) {
            subscribe?.accept(pushEvent)
    }

    fun subscribe(consumer: Consumer<EventModel>) {
        this.subscribe = consumer
    }
    fun unsubscribe() {
        this.subscribe = null
    }
}