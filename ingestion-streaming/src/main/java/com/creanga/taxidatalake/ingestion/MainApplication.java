package com.creanga.taxidatalake.ingestion;

import com.creanga.taxidatalake.ingestion.conf.Configuration;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Level;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainApplication {

  private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

  public static void main(String[] args) throws TimeoutException, StreamingQueryException {

    Configuration conf = new Configuration(ConfigFactory.load());

    logger.info("---Configuration---");
    logger.info("kafka brokers: " + conf.getBrokers());
    logger.info("kafka topic: " + conf.getTopic());

    SparkSession sparkSession = SparkSession.builder()
        .master("local[2]")
        .appName("Spark streaming")
        .config("spark.some.config.option", "some-value")
        .getOrCreate();

    JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext());
//    sc.setLogLevel("INFO");
//    org.apache.log4j.Logger.getLogger("org.apache.kafka.clients.consumer").setLevel(Level.OFF);
//    org.apache.log4j.Logger.getLogger("org.apache.spark.scheduler").setLevel(Level.OFF);
//    org.apache.log4j.Logger.getLogger("org.apache.spark").setLevel(Level.OFF);
//    org.apache.log4j.Logger.getLogger("org.apache.spark.sql").setLevel(Level.WARN);

    Dataset<Row> df = sparkSession.readStream().format("kafka")
        .option("kafka.bootstrap.servers", conf.getBrokers())
        .option("subscribe", conf.getTopic())
        .option("startingOffsets", "latest")
        .option("failOnDataLoss", "false")
        .option("minPartitions", "32")
        .load();

    BatchProcessor batchProcessor = new BatchProcessor();
    // Transform and write batchDF
    StreamingQuery query = df.writeStream()
        .foreachBatch((VoidFunction2<Dataset<Row>, Long>) batchProcessor::processBatchData)
        .trigger(Trigger.ProcessingTime(60, TimeUnit.SECONDS))
        .start();

    query.awaitTermination();
  }

}
