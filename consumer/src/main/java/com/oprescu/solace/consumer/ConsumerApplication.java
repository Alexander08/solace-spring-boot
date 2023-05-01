package com.oprescu.solace.consumer;

import javax.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication.class, args);
  }

}

@Slf4j
@Component
class MessageReceiver {

  @JmsListener(destination = "SOME_MESSAGE_QUEUE")
  public void handleSomeMessage(Message message) {
    log.info("<<<<<<<<<<<<< RECEIVED SOME MESSAGE: [{}]", message);
  }

  @JmsListener(destination = "OTHER_MESSAGE_QUEUE")
  public void handleOtherMessage(Message message) {
    log.info("<<<<<<<<<<<<< RECEIVED OTHER MESSAGE: [{}]", message);
  }
}

