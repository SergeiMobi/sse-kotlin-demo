package com.mobiquity.sse.repository

import com.mobiquity.sse.model.EventModel
import org.springframework.stereotype.Repository
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
interface EventRepository : MongoRepository<EventModel, String?>