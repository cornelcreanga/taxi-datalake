package com.creanga.taxidatalake.tripgenerator;

import com.typesafe.config.Config;

public class Configuration {

    private final Config config;

    private final String brokers;
    private final String topic;
    private final int producerThreads;
    private final int tripsPerThread;

    public Configuration(Config config) {
        this.config = config;
        brokers = config.getString("kafka.brokers");
        topic = config.getString("kafka.topic");
        producerThreads = config.getInt("producerThreads");
        tripsPerThread = config.getInt("tripsPerThread");
    }

    public String getBrokers() {
        return brokers;
    }

    public String getTopic() {
        return topic;
    }

    public int getProducerThreads() {
        return producerThreads;
    }

    public int getTripsPerThread() {
        return tripsPerThread;
    }
}
