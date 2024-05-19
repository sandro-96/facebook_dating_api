package com.fbd.config;

import com.fbd.model.User;
import com.mongodb.client.model.Indexes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;

import javax.annotation.PostConstruct;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

}
