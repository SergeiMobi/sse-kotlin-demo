package com.mobiquity.sse.repository

import com.mobiquity.sse.model.Event
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.stereotype.Component


@Component
class MongoListener : AbstractMongoEventListener<Event?>() {
    override fun onAfterSave(event: AfterSaveEvent<Event?>) {
        println("" + event.getSource())
        println("" + event.getDocument())
    }
}