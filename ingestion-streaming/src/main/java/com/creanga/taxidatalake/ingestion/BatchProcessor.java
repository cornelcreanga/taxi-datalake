package com.creanga.taxidatalake.ingestion;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;


public class BatchProcessor {

  public void processBatchData(Dataset<Row> df, Long batchId) {
    JavaRDD<Row> rdd = df.javaRDD();

    JavaRDD<byte[]> t = rdd.map((Function<Row, byte[]>) v1 -> v1.getAs("value"));
  }
//Function
}

/**
 * df.rdd().map(new MapFunction<String, byte[]>(){
 *
 * @Override public byte[] call(String value) throws Exception {
 * return new byte[0];
 * }
 * } );
 */

//(MapFunction<String, byte[]>) value ->  Encoders.STRING(),value.getBytes()

//  def processBatchData(df: DataFrame,batchId: Long): Unit = {
//    val rdd = df.rdd
//    val events: RDD[(Integer, Object)] = rdd.map {
//    r => {
//    val bytes = r.getAs[Array[Byte]](r.fieldIndex("value"))
//    try {
//    val event = mapper.readValue(bytes, classOf[TurnstileEvent])
//    (event.code, event)
//    } catch {
//    case NonFatal(e) => (TurnstileEvent.NOT_PARSABLE, bytes)
//    }
//    }
//    }
//    }
