package com.creanga.taxidatalake.tripgenerator;

import com.creanga.taxidatalake.protobuftrip.Trip;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.UUID;

public class TripGenerator implements Runnable{

  private final Producer<byte[],byte[]> kafkaProducer;
  public final String topic;
  public final int trips;


  public TripGenerator(Producer<byte[],byte[]> kafkaProducer, String topic, int trips) {
    this.kafkaProducer = kafkaProducer;
    this.topic = topic;
    this.trips = trips;
  }

  @Override
  public void run() {

    for (int i = 0; i < trips; i++) {
      Trip.TripStart tripStart = tripStart();
      kafkaProducer.send(new ProducerRecord<>(topic, tripStart.toByteArray()));

      kafkaProducer.flush();
    }

  }

  public Trip.TripStart tripStart(){
    return Trip.TripStart.newBuilder().
        setTripId(UUID.randomUUID().toString()).
        setDriverId("").
        setUserId("").
        setLatitude("").
        setLongitude("").
        setOrderTimestamp(System.currentTimeMillis()).
        setStartTimestamp(System.currentTimeMillis()).
        setPaymentType(Trip.TripStart.PaymentType.CARD).
        build();
  }
}
