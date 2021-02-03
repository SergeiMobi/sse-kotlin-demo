package com.mobiquity.sse.service.pubsub

import com.mobiquity.sse.model.EventModel
import org.springframework.stereotype.Service

@Service
class PubSubService {

    private val subscribers = ArrayList<Subscriber>()

    fun sendMessage(event: EventModel) {
        for (subscriber in subscribers) {
            subscriber.receivedMessage(event)
        }
    }

    fun registerSubscriber(subscriber: Subscriber) {
        subscribers.add(subscriber)
    }
}