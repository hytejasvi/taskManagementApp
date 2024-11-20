package com.hytejasvi.taskManagementApp.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.bson.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class
UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Map<String, List<String>> getUsersForMailNotification(Date threshold) {
        log.info("Threshold Date for query: {}", threshold);

        Aggregation aggregation = Aggregation.newAggregation(
                // Unwind tasks array to process individual tasks
                Aggregation.unwind("tasks"),

                // Perform lookup to join the task data (dereferencing @DBRef)
                Aggregation.lookup("task_Entries", "tasks", "_id", "taskDetails"),

                // Match tasks that are incomplete and have a deadline <= threshold
                Aggregation.match(Criteria.where("taskDetails.isCompleted").ne(true)
                        .and("taskDetails.deadline").lte(threshold)),

                // Project relevant fields: mailId and extract task details using $arrayElemAt
                Aggregation.project("mailId")
                        .andExpression("arrayElemAt(taskDetails, 0)").as("taskDetails"),

                // Group by mailId and push task titles into a list
                Aggregation.group("mailId")
                        .push("taskDetails.title").as("title")
        );

        log.info("Aggregation pipeline: {}", aggregation.toString());

        // Execute the aggregation
        List<Document> pipelineResults = mongoTemplate.aggregate(aggregation, "user", Document.class).getMappedResults();
        pipelineResults.forEach(doc -> log.info("Intermediate result: {}", doc));

        Map<String, List<String>> userNotifications = new HashMap<>();

        // Extract the results and build the final map
        for (Document doc : pipelineResults) {
            String mailId = doc.getString("_id");
            List<String> titles = (List<String>) doc.get("title");
            userNotifications.put(mailId, titles);
        }

        log.info("Final user notifications: {}", userNotifications);
        return userNotifications;
    }
}
