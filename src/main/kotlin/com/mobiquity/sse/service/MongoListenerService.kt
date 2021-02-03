package com.mobiquity.sse.service

import com.mobiquity.sse.model.EventModel
import com.mobiquity.sse.service.pubsub.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.stereotype.Component

@Component
class MongoListenerService(val publisher: Publisher) : AbstractMongoEventListener<EventModel?>() {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun onAfterSave(event: AfterSaveEvent<EventModel?>) {
        log.debug("MongoDb listener service document: " + event.document)
        publisher.publish(EventModel(event.document.toString()))
    }
}