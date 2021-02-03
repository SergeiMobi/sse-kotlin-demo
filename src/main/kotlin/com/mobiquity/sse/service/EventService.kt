package com.mobiquity.sse.service

import com.mobiquity.sse.dto.EventDto
import com.mobiquity.sse.model.EventModel
import com.mobiquity.sse.repository.EventRepository
import org.springframework.stereotype.Service

@Service
class EventService constructor(val eventRepository: EventRepository) {

    fun addEvent(event: EventDto) {
        eventRepository.insert(EventModel(event.payload))
    }
}