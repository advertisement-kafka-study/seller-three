package com.example.seller;

import io.cloudevents.v03.CloudEventBuilder;
import io.cloudevents.v03.CloudEventImpl;
import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@Singleton
@Slf4j
public class Service {

  private final AtomicInteger counter = new AtomicInteger(1);

  @ConfigProperty(name = "quarkus.application.name")
  private String applicationName;

  @Outgoing("advertisement")
  public Flowable<KafkaRecord<String, CloudEventImpl<Advertisement>>> publishMessage() {
    return Flowable.interval(1, TimeUnit.SECONDS)
        .onBackpressureDrop()
        .map(value -> {
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
        });
  }

}