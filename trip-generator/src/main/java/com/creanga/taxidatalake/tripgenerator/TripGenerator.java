package com.creanga.taxidatalake.tripgenerator;

import com.creanga.taxidatalake.protobuftrip.Trip;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.UUID;

public class TripGenerator implements Runnable{

  public static final double northLatitude = 44.481230877283856d;
  public static final double southLatitude = 44.38316703371887;
  public static final double westLongitude = 26.1745387998047d;
  public static final double eastLongitude = 26.031888195556654d;

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
        setStartLatitude(0).
        setStartLongitude(0).
        setEndLatitude(0).
        setEndLongitude(0).
        setOrderTimestamp(System.currentTimeMillis()).
        setTripTimestamp(System.currentTimeMillis()).
        setPaymentType(Trip.TripStart.PaymentType.CARD).
        build();
  }
}
