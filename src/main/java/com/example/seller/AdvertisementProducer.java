package com.example.seller;

import io.cloudevents.v03.CloudEventBuilder;
import io.cloudevents.v03.CloudEventImpl;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.reactive.messaging.kafka.OutgoingKafkaRecord;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
@Slf4j
public class AdvertisementProducer {

  private final AtomicInteger counter = new AtomicInteger(1);

  @ConfigProperty(name = "quarkus.application.name")
  private String applicationName;

  public OutgoingKafkaRecord<String, CloudEventImpl<Advertisement>> publishMessage() {

    Advertisement advertisement = new Advertisement();
    advertisement.setId(String.valueOf(counter.getAndIncrement()));
    advertisement.setName(applicationName);

    CloudEventImpl<Advertisement> cloudEvent = CloudEventBuilder.<Advertisement>builder()
        .withId(UUID.randomUUID().toString())
        .withSource(URI.create("/advertisements/" + advertisement.getId()))
        .withType("Advertisement")
        .withTime(ZonedDateTime.now(ZoneOffset.UTC))
        .withData(advertisement)
        .build();

    log.info("Published Message=[{}] to Topic=[{}]", advertisement, "xxx");
    return KafkaRecord.of(applicationName, cloudEvent);
  }

}