package fr.esgi.dvf.config;

import jakarta.jms.Queue;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JMSConfig {
    @Bean
    public Queue queue() {
        return new ActiveMQQueue("generatePdf");
    }
}