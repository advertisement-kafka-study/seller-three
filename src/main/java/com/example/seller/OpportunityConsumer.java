package com.example.seller;

import io.cloudevents.v03.CloudEventImpl;
import io.smallrye.reactive.messaging.kafka.OutgoingKafkaRecord;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@Singleton
@Slf4j
public class OpportunityConsumer {

  @Inject
  private AdvertisementProducer advertisementProducer;

  @Incoming("opportunity")
  @Outgoing("advertisement")
  public OutgoingKafkaRecord<String, CloudEventImpl<Advertisement>> process(
      Opportunity opportunity) {
    log.info("Consuming Message=[{}]", opportunity);
    return advertisementProducer.publishMessage(opportunity);
  }

}