package fr.ryu.followthing.config.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

/**
 * Created by Ryuko on 21/12/2014.
 */
@Configuration
public class ServiceBeanFactory {

    @Bean
    protected ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    protected MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient();
    }
}
