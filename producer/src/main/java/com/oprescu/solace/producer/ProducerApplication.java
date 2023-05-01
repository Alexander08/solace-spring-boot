package com.oprescu.solace.producer;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@SpringBootApplication
public class ProducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProducerApplication.class, args);
  }

}

@Configuration
@RequiredArgsConstructor
class SolaceConfiguration {

  private final JmsTemplate jmsTemplate;

  @PostConstruct
  public void customizeJmsTemplate() {
    // Update the jmsTemplate's connection factory to cache the connection
    CachingConnectionFactory ccf = new CachingConnectionFactory();
    ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());
    jmsTemplate.setConnectionFactory(ccf);

    // By default, Spring Integration uses Queues, but if you set this to true you
    // will send to a PubSub+ topic destination
    jmsTemplate.setPubSubDomain(true);
  }
}

@Component
@RequiredArgsConstructor
@Slf4j
class MessageSender {

  private final JmsTemplate solaceJmsTemplate;

  @Scheduled(fixedRate = 10000)
  public void messageSender() {

    Message message = Math.random() < 0.5 ? getSomeMessage() : getOtherMessage();

    String topicName = "domain.org/" + message.getClass().getSimpleName() + "/" + message.getName();

    log.info("###### SEND MESSAGE to TOPIC [{}] with payload [{}]", topicName, message);
    solaceJmsTemplate.convertAndSend(topicName, message.toString());
  }

  private static SomeMessage getSomeMessage() {
    return SomeMessage.builder()
        .id((long) (Math.random() * 10_000))
        .name(UUID.randomUUID().toString())
        .creationDate(OffsetDateTime.now())
        .build();
  }

  private static OtherMessage getOtherMessage() {
    return OtherMessage.builder()
        .id((long) (Math.random() * 10_000))
        .name(UUID.randomUUID().toString())
        .creationDate(OffsetDateTime.now())
        .build();
  }
}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SomeMessage implements Message {

  private long id;
  private String name;
  private OffsetDateTime creationDate;
}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OtherMessage implements Message {

  private long id;
  private String name;
  private OffsetDateTime creationDate;
}

interface Message {
  String getName();
}