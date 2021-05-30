package com.creanga.taxidatalake.ingestion.conf;

import com.typesafe.config.Config;

public class Configuration {

    private final Config config;

    private final String brokers;
    private final String topic;

    public Configuration(Config config) {
        this.config = config;
        brokers = config.getString("kafka.brokers");
        topic = config.getString("kafka.topic");
    }

    public String getBrokers() {
        return brokers;
    }

    public String getTopic() {
        return topic;
    }
}
