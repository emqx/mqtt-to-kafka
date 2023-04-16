# mqtt-to-kafka

Streaming IoT data into Confluent/Kafka using MQTT and EMQX | MQTT Kafka Integration

## Introduction

You can use EMQX [data integration](https://www.emqx.com/en/solutions/mqtt-data-integration) or write your own application to achieve this task. This project provides the corresponding sample code.

## Prerequisites

- Node.js: v14.15.4+
- Java: 1.8+
- EMQX: 5.0.0+
- Kafka: 2.8.0+

## Docker Compose

You can use Docker Compose to start this project.

```bash
docker compose up -d

docker exec -it kafka \
  kafka-topics --create --topic my-kafka-topic --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

docker ps -a
```

## Installation

1. Start confluent with Docker:

```bash
# Create a network
docker network create emqx-net

# Start Zookeeper
docker run -d --name zookeeper \
  --network emqx-net \
  -e ZOOKEEPER_CLIENT_PORT=2181 \
  -p 2181:2181 \
  confluentinc/cp-zookeeper

# Start Kafka with Zookeeper
docker run -d --name kafka \
  --network emqx-net \
  -p 9092:9092 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  confluentinc/cp-kafka
```

2. Create `my-kafka-topic` topic:

```bash
docker exec -it kafka \
  kafka-topics --create --topic my-kafka-topic --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092
```

3. Start EMQX with Docker:

```bash
# Generate a bootstrap API key if you want to use EMQX data integration
echo "myapi:$(uuidgen)" > bootstrap_api_key.conf

docker run -d --name emqx-enterprise \
  --network emqx-net \
  -p 1883:1883 -p 8083:8083 -p 8084:8084 \
  -p 8883:8883 -p 18083:18083 \
  -e EMQX_API_KEY__BOOTSTRAP_FILE=etc/bootstrap_api_key.conf \
  -v $PWD/bootstrap_api_key.conf:/opt/emqx/etc/bootstrap_api_key.conf \
  emqx/emqx-enterprise:5.0.2
```

4. Install [IoT Solumation](https://github.com/wivwiv/iot-simulator) with Docker:

```bash
docker pull docker.pkg.github.com/wivwiv/iot-simulator/iot-simulator
```

## Usage

1. Start MQTT to Kafka application or create a EMQX integration, it will subscribe `iot_simulator/#`, `t/#` topic and streaming message into Kafka `my-kafka-topic` topic:

- [Data Integration](./data-bridge/README.md)

  Create data integration and rule.

- [Node.js](./nodejs/README.md)

  ```bash
  cd nodejs
  node index.js
  ```

- [Java](./java/README.md)

  ```bash
  cd java
  TODO
  ```

2. Start IoT Solumation, it will generate random IoT data and send to EMQX `iot_simulator/<clientid>` topic:

```bash
docker run -it --rm --network emqx-net \
  docker.pkg.github.com/wivwiv/iot-simulator/iot-simulator bash
docker run -it --rm --network emqx-net docker.pkg.github.com/wivwiv/iot-simulator/iot-simulator bash
  
> a77c057555b9:/app# 
iot-simulator --host emqx-enterprise --sense tesla --count 20
```

3. Start Kafka consumer to consume message from `my-kafka-topic` topic:

```bash
docker exec -it kafka \
  kafka-console-consumer \
  --topic my-kafka-topic \
  --from-beginning \
  --bootstrap-server localhost:9092
```
