package com.hytejasvi.taskManagementApp.repository;

import com.hytejasvi.taskManagementApp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class
UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForMailNotification() {
        Query query = new Query();
        //query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$"));
        query.addCriteria(Criteria.where("tasks").ne(null));

        log.info("Query is: "+query.toString());

        List<User> users = mongoTemplate.find(query, User.class);
        log.info("returned users list is: "+users);

        return users;
    }
}
