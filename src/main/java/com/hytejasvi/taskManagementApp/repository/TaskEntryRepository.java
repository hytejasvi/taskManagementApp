package com.hytejasvi.taskManagementApp.repository;

import com.hytejasvi.taskManagementApp.entity.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskEntryRepository extends MongoRepository<Task, ObjectId> {
}
