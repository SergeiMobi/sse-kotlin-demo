package com.mobiquity.sse.service

import com.mobiquity.sse.model.EventModel
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.stereotype.Component


@Component
class MongoListenerService(val publisher: Publisher) : AbstractMongoEventListener<EventModel?>() {
    override fun onAfterSave(event: AfterSaveEvent<EventModel?>) {
        println("" + event.getSource())
        println("" + event.getDocument())
        publisher.publish(EventModel(event.document.toString()))
    }
}