package com.creanga.taxidatalake.ingestion.conf;

import com.typesafe.config.Config;

public class Configuration {

    private final String brokers;
    private final String topic;
    private final String hadoopFs;

    public Configuration(Config config) {
        brokers = config.getString("kafka.brokers");
        topic = config.getString("kafka.topic");
        hadoopFs = config.getString("hadoop.fs");
    }

    public String getBrokers() {
        return brokers;
    }

    public String getTopic() {
        return topic;
    }

    public String getHadoopFs() {
        return hadoopFs;
    }
}
