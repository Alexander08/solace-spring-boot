package com.oprescu.solace.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class JmsMessageSender {

  private final JmsTemplate solaceJmsTemplate;
  private final ObjectMapper objectMapper;

  @Scheduled(fixedRate = 10000)
  public void messageSender() throws JsonProcessingException {

    Message message = OtherMessage.builder()
        .id((long) (Math.random() * 10_000))
        .name(UUID.randomUUID().toString())
        .creationDate(OffsetDateTime.now())
        .build();

    String topicName = "domain.org/" + message.getClass().getSimpleName() + "/" + message.getName();
    String payload = objectMapper.writeValueAsString(message);

    log.info("###### SEND MESSAGE to TOPIC [{}] with payload [{}]", topicName, message);
    solaceJmsTemplate.convertAndSend(topicName, payload);
  }

}
