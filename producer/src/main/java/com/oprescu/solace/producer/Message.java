package com.oprescu.solace.producer;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface Message {

  String getName();
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
