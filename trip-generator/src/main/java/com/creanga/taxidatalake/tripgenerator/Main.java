package com.creanga.taxidatalake.tripgenerator;

import com.typesafe.config.ConfigFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

public class Main {

  public static void main(String[] args) {
    Configuration conf = new Configuration(ConfigFactory.load());
    Properties props = new Properties();

    //Assign localhost id
    props.put("bootstrap.servers", "localhost:9092");

    props.put("acks", "all");
    props.put("retries", 0);
    props.put("batch.size", 16384);
    props.put("linger.ms", 1);
    props.put("buffer.memory", 33554432);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<byte[],byte[]> producer = new KafkaProducer<>(props);
    for (int i = 0; i < conf.getProducerThreads(); i++) {
       TripGenerator tripGenerator = new TripGenerator(producer, conf.getTopic(), conf.getTripsPerThread());
       new Thread(tripGenerator).start();
    }

    producer.close();
  }

}
