package com.oprescu.solace.producer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.SDTException;
import com.solacesystems.jcsmp.SDTMap;
import com.solacesystems.jcsmp.XMLMessageProducer;
import com.solacesystems.jcsmp.impl.sdt.MapImpl;
import javax.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@RequiredArgsConstructor
class SolaceConfiguration {

  public static final SDTMap defaultMessageProperties;

  static {
    defaultMessageProperties = new MapImpl();
    try {
      defaultMessageProperties.putBoolean("JMS_Solace_isXML", false);
    } catch (SDTException e) {
      throw new RuntimeException(e);
    }
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    return objectMapper;
  }

  @Bean
  public JmsTemplate solaceJmsTemplate(ConnectionFactory connectionFactory) {
    JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
    // Update the jmsTemplate's connection factory to cache the connection
    CachingConnectionFactory ccf = new CachingConnectionFactory();
    ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());
    jmsTemplate.setConnectionFactory(ccf);
    // By default, Spring Integration uses Queues, but if you set this to true you
    // will send to a PubSub+ topic destination
    jmsTemplate.setPubSubDomain(true);
    return jmsTemplate;
  }

  @Bean
  public JCSMPSession jcsmpSession(JCSMPProperties jcsmpProperties) throws InvalidPropertiesException {
    return JCSMPFactory.onlyInstance().createSession(jcsmpProperties);
  }

  @Bean
  public XMLMessageProducer xmlMessageProducer(JCSMPSession jcsmpSession) throws JCSMPException {
    return jcsmpSession.getMessageProducer(new DefaultPublisherHandler());
  }
}
