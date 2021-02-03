package com.mobiquity.sse.service

import com.mobiquity.sse.model.Event
import org.springframework.stereotype.Service

@Service
class PubSubService {

    private val subscribers = ArrayList<Subscriber>()

    fun sendMessage(event: Event) {
        for (subscriber in subscribers) {
            subscriber.receivedMessage(event)
        }
    }

    fun registerSubscriber(subscriber: Subscriber) {
        subscribers.add(subscriber)
    }
}