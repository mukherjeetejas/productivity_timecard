package com.personal.timecard.productivity_timecard.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import static com.personal.timecard.productivity_timecard.constant.ApplicationConstants.CONNECTION_STRING;

@Configuration
class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${db.user}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.cluster}")
    private String cluster;

    @Value("${db.database}")
    private String database;


    @Override
    protected @NonNull String getDatabaseName() {
        return database;
    }


    @Bean
    @Override
    public @NonNull MongoClient reactiveMongoClient() {
        String connectionString = String.format(
                CONNECTION_STRING,
                username,
                password,
                cluster
        );
        return MongoClients.create(connectionString);
    }
}