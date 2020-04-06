package com.example.seller;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
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
  public Flowable<KafkaRecord<String, Advertisement>> publishMessage() {
    return Flowable.interval(1, TimeUnit.SECONDS)
        .onBackpressureDrop()
        .map(value -> {
          Advertisement advertisement = new Advertisement();
          advertisement.setId(String.valueOf(counter.getAndIncrement()));
          advertisement.setName(applicationName);

          log.info("Published Message=[{}] to Topic=[{}]", advertisement, "xxx");
          return KafkaRecord.of(applicationName, advertisement);
        });
  }

}