package com.creanga.taxidatalake.tripgenerator;

import com.creanga.taxidatalake.protobuftrip.Trip;

import java.util.List;

public class Itinerary {

  public Trip.TripStart tripStart;
  public List<Trip.TripPing> tripPing;
  public Trip.TripEnd tripEnd;
  public Trip.TripStop tripStop;

}
