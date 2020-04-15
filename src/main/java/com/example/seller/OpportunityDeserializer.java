package com.example.seller;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OpportunityDeserializer extends ObjectMapperDeserializer<Opportunity> {

  public OpportunityDeserializer() {
    super(Opportunity.class);
  }

}

