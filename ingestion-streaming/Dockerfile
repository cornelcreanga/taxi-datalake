FROM bitnami/spark:3.1.1

VOLUME /streaming

ENV MASTER_URL=spark://localhost:7077
ENV CLASS=com.creanga.datalake.ingestion.MainApplication
ENV DRIVER_HOST=spark-submit
ENV DRIVER_PORT=40694

RUN mkdir /opt/application
COPY target/ingestion-streaming-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/application/app.jar

CMD /opt/bitnami/spark/bin/spark-submit --class $CLASS --master $MASTER_URL --deploy-mode client /opt/application/app.jar --conf "spark.driver.extraJavaOptions=-Dlog4jspark.root.logger=WARN,console"
