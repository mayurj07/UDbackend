package com.udelvr;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.index.GeospatialIndex;

import javax.servlet.MultipartConfigElement;


@EnableAutoConfiguration
@ComponentScan
@Configuration
public class UdelvrConfig {

    @Bean
    public MongoTemplate mongoTemplate() throws Exception
    {
        MongoClientURI uri = new MongoClientURI("mongodb://varuna:varuna@ds061691.mongolab.com:61691/udelvr");
        MongoDbFactory mongoDbFactory =  new SimpleMongoDbFactory(new MongoClient(uri),"udelvr");
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
        return mongoTemplate;

      /*  MongoClient mongoClient = new MongoClient("",27017);//localhost
        UserCredentials userCredentials = new UserCredentials("","");
        MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, "udelvr",userCredentials) );
        return mongoTemplate;*/
    }
/*
    @Bean
    public MongoOperations MongoOperations() throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(UdelvrConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        return mongoOperation;
    }
*/

    @Bean
    public DBCollection collectionSettings() throws Exception {
        MongoTemplate mongoTemplate = mongoTemplate();
        DBCollection collection = mongoTemplate.getCollection("shipment");
        //collection.ensureIndex(new GeospatialIndex("sourceLocation"));
        collection.ensureIndex(new BasicDBObject("sourceLocation", "2dsphere"), "sourceLocationIndex");
        return collection;
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("20MB");
        factory.setMaxRequestSize("20MB");
        return factory.createMultipartConfig();
    }

}
