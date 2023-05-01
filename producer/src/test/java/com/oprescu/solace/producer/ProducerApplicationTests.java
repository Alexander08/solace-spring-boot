package com.oprescu.solace.producer;

import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.XMLMessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@MockBean({
    JCSMPSession.class,
    XMLMessageProducer.class
})
class ProducerApplicationTests {

  @Test
  void contextLoads() {
  }

}
