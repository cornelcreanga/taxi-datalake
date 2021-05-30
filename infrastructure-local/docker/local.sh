cd ../../
mvn clean package
cd files-api
docker build -t filesapi .
cd ../files-worker
docker build -t filesworker .

cd ../files-local-infra/docker

echo "copying prometheus configuration"
mkdir -p /var/local/docker/msengg/volumes/prometheus
cp prometheus/prometheus.yml /var/local/docker/msengg/volumes/prometheus/.
chmod -R 777 /var/local/docker/msengg/volumes/prometheus/

docker-compose up
