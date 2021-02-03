package com.mobiquity.sse.service

import com.mobiquity.sse.model.Event
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.stereotype.Component


@Component
class MongoListenerService(val publisher: Publisher) : AbstractMongoEventListener<Event?>() {
    override fun onAfterSave(event: AfterSaveEvent<Event?>) {
        println("" + event.getSource())
        println("" + event.getDocument())
        publisher.publish(Event(event.document.toString()))
    }
}