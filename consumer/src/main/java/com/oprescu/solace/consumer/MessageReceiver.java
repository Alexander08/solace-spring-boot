package com.oprescu.solace.consumer;

import com.solacesystems.jms.message.SolBytesMessage;
import com.solacesystems.jms.message.SolTextMessage;
import java.nio.charset.StandardCharsets;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class MessageReceiver {

  @JmsListener(destination = "SOME_MESSAGE_QUEUE")
  public void handleSomeMessage(Message message) throws JMSException {
    log.info("<<<<<<<<<<<<< RECEIVED SOME MESSAGE: [{}] with payload [{}]", message, getPayload(message));
  }

  @JmsListener(destination = "OTHER_MESSAGE_QUEUE")
  public void handleOtherMessage(Message message) throws JMSException {
    log.info("<<<<<<<<<<<<< RECEIVED OTHER MESSAGE: [{}] with payload [{}]", message, getPayload(message));
  }

  private String getPayload(Message message) throws JMSException {
    if (message instanceof SolBytesMessage) {
      SolBytesMessage solBytesMessage = (SolBytesMessage) message;
      final byte[] bytes = new byte[(int) solBytesMessage.getBodyLength()];
      solBytesMessage.readBytes(bytes);
      return StringUtils.toEncodedString(bytes, StandardCharsets.UTF_8);
    } else if (message instanceof SolTextMessage) {
      return ((SolTextMessage) message).getText();
    } else if (message instanceof TextMessage) {
      return ((TextMessage) message).getText();
    } else {
      log.error("Unknown message received. [{}]", message);
      return null;
    }
  }
}
