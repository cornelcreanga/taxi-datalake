package com.creanga.taxidatalake.tripgenerator;

import com.creanga.taxidatalake.protobuftrip.Trip;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TripGenerator implements Runnable {

  public static final double northLatitude = 44.481230877283856d;
  public static final double southLatitude = 44.38316703371887;
  public static final double westLongitude = 26.1745387998047d;
  public static final double eastLongitude = 26.031888195556654d;

  private final Producer<byte[], byte[]> kafkaProducer;
  public final String topic;
  public final int trips;


  public TripGenerator(Producer<byte[], byte[]> kafkaProducer, String topic, int trips) {
    this.kafkaProducer = kafkaProducer;
    this.topic = topic;
    this.trips = trips;
  }

  @Override
  public void run() {

    for (int i = 0; i < trips; i++) {
      Itinerary itinerary = buildItinerary();
      kafkaProducer.send(new ProducerRecord<>(topic, itinerary.tripStart.toByteArray()));
      for (int j = 0; i < itinerary.tripPing.size(); j++) {
        sleep(RandomUtils.nextLong(1000, 5000));
        kafkaProducer.send(new ProducerRecord<>(topic, itinerary.tripPing.get(j).toByteArray()));
        kafkaProducer.flush();
      }
      kafkaProducer.send(new ProducerRecord<>(topic, itinerary.tripEnd.toByteArray()));
      kafkaProducer.flush();

//      kafkaProducer.send(new ProducerRecord<>(topic, tripStart.toByteArray()));
//      kafkaProducer.flush();
    }
  }

  private void sleep(long l){
    try {
      Thread.sleep(l);
    } catch (InterruptedException e) {
    }
  }

  public Itinerary buildItinerary() {
    Itinerary itinerary = new Itinerary();
    String driverId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    long start = minutesBack(RandomUtils.nextLong(10, 60));
    long end = System.currentTimeMillis();
    String tripId = UUID.randomUUID().toString();
    itinerary.tripStart = tripStart(tripId, start, driverId, userId);

    itinerary.tripEnd = tripEnd(tripId, end);
//      if (RandomUtils.nextLong(1, 100) == 1) {
//        long stop = start + RandomUtils.nextLong(400000, 600000);
//        itinerary.tripStop = Trip.TripStop.newBuilder().
//            setTripId(tripId).
//            setLatitude(itinerary.tripStart.getStartLatitude() - RandomUtils.nextDouble(0, 0.001)).
//            setLongitude(itinerary.tripStart.getStartLongitude() - RandomUtils.nextDouble(0, 0.001)).
//            setStopTimestamp(stop).
//            setStartTimestamp(stop + RandomUtils.nextLong(30000, 60000)).
//            build();
//      }

    long steps = (end - start) / (30 * 1000);
    List<Trip.TripPing> tripPings = new ArrayList<>();
    long lastTimestamp = itinerary.tripStart.getTripTimestamp();

    double lastLatitude = itinerary.tripStart.getStartLatitude();
    double lastLongitude = itinerary.tripStart.getStartLongitude();
    for (int j = 0; j < steps; j++) {
      long timestamp = lastTimestamp + (30 * 1000);
      double latitude = lastLatitude - RandomUtils.nextDouble(0, 0.001);
      double longitude = lastLongitude - RandomUtils.nextDouble(0, 0.001);
      tripPings.add(Trip.TripPing.newBuilder().
          setTripId(tripId).
          setTimestamp(timestamp).
          setLatitude(latitude).
          setLongitude(longitude).
          build());
      lastLatitude = latitude;
      lastLongitude = longitude;
      lastTimestamp = timestamp;
    }
    itinerary.tripPing = tripPings;
    return itinerary;
  }

  public Trip.TripStart tripStart(String tripId, Long start, String driverId, String userId) {
    return Trip.TripStart.newBuilder().
        setTripId(tripId).
        setDriverId(driverId).
        setUserId(userId).
        setStartLatitude(northLatitude - RandomUtils.nextDouble(0, 0.001)).
        setStartLongitude(westLongitude - RandomUtils.nextDouble(0, 0.001)).
        setEndLatitude(southLatitude).
        setEndLongitude(eastLongitude).
        setOrderTimestamp(start - RandomUtils.nextLong(10000, 60000)).
        setTripTimestamp(start).
        setPaymentType(Trip.TripStart.PaymentType.CARD).
        build();
  }

  public Trip.TripEnd tripEnd(String tripId, Long end) {
    return Trip.TripEnd.newBuilder().
        setTripId(tripId).
        setTimestamp(end).
        build();
  }

  public long minutesBack(long minutes) {
    return System.currentTimeMillis() - minutes * 60 * 1000 * 1000;
  }

}
