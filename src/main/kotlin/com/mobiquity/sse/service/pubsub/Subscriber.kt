package com.mobiquity.sse.service.pubsub

import com.mobiquity.sse.model.EventModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class Subscriber(pubSubService: PubSubService) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    private var subscribe: Consumer<EventModel>? = null

    init {
        pubSubService.registerSubscriber(this)
    }

    fun receivedMessage(pushEvent: EventModel) {
        log.debug("subscribe event is received: $pushEvent")
        subscribe?.accept(pushEvent)
    }

    fun subscribe(consumer: Consumer<EventModel>) {
        this.subscribe = consumer
    }

    fun unsubscribe() {
        this.subscribe = null
    }
}