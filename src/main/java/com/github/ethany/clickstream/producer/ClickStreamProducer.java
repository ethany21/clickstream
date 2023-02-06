package com.github.ethany.clickstream.producer;

import clickstream.events;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class ClickStreamProducer {

    @Autowired
    private KafkaTemplate<String, events> kafkaTemplate;

    @EventListener(ApplicationStartedEvent.class)
    public void generator() {

        List<String> status_code = Arrays.asList("200", "302", "404", "405", "406", "407");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        Faker faker = new Faker();
        while (true) {
            events event = events.newBuilder()
                    .setIp(faker.internet().ipV4Address())
                    .setUserid(ThreadLocalRandom.current().nextInt((int) Math.pow(10, 6), (int) Math.pow(10, 7)))
                    .setRemoteUser("-")
                    .setTime(dtf.format(LocalDateTime.now()))
                    .setTime$1(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .setRequest("GET " + faker.internet().url())
                    .setStatus(status_code.get(ThreadLocalRandom.current().nextInt(0, 5 + 1)))
                    .setBytes(String.valueOf(ThreadLocalRandom.current().nextInt((int) Math.pow(10, 2), (int) Math.pow(10, 5))))
                    .setReferrer("-").setAgent(faker.internet().userAgent(null))
                    .build();

            kafkaTemplate.send("clickstream", event);
        }

    }


}
