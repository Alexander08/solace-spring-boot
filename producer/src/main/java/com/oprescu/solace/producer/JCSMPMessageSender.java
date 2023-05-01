package com.oprescu.solace.producer;

import static com.oprescu.solace.producer.SolaceConfiguration.defaultMessageProperties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPStreamingPublishCorrelatingEventHandler;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageProducer;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JCSMPMessageSender {

  private final ObjectMapper objectMapper;
  private final XMLMessageProducer messageProducer;

  @Scheduled(fixedRate = 9_000)
  public void messageSender() throws JCSMPException, JsonProcessingException {

    Message payload = SomeMessage.builder()
        .id((long) (Math.random() * 10_000))
        .name(UUID.randomUUID().toString())
        .creationDate(OffsetDateTime.now())
        .build();

    TextMessage message = createJsonMessage(payload);

    String topicName = "domain.org/" + payload.getClass().getSimpleName() + "/" + payload.getName();
    Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);

    log.info("###### SEND MESSAGE to TOPIC [{}] with payload [{}]", topicName, payload);
    messageProducer.send(message, topic);
  }

  private TextMessage createJsonMessage(Object payload) throws JsonProcessingException {
    TextMessage message = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
    message.setProperties(defaultMessageProperties);
    String serializedPayload = objectMapper.writeValueAsString(payload);
    message.setText(serializedPayload);
    return message;
  }
}

@Slf4j
class DefaultPublisherHandler implements JCSMPStreamingPublishCorrelatingEventHandler {

  @Override
  public void responseReceivedEx(Object o) {
    log.info("OK response invoked: responseReceivedEx called {}", o);
  }

  @Override
  public void handleErrorEx(Object o, JCSMPException e, long l) {
    log.error("Error {} for object {}", e.getMessage(), o, e);
  }
}
