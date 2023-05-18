# mqtt-to-kafka

Streaming IoT data into wurstmeister/kafka using MQTT and EMQX | MQTT Kafka Integration

## TODO

1. Add MQTTX CLI and run simulate MQTT client to publish messages to EMQX
2. Init Grafana dashboard for data visualization
3. Auto create kafka topic when start kafka container

## Introduction

You can use EMQX [data integration](https://www.emqx.com/en/solutions/mqtt-data-integration) or write your own application to achieve this task. This project provides the corresponding sample code.

## Docker Compose

You can use Docker Compose to quick start this project.

1. Start confluent with Docker Compose:

```bash
docker compose up -d
```

2. View continer status:

```bash
docker compose ps
```

3. If all containers are running, you can access the following services:

```bash
docker exec -it kafka \
  kafka-console-consumer.sh \
  --topic my-vehicles \
  --from-beginning \
  --bootstrap-server localhost:9092
```

You can see the message from Kafka consumer:

```json
{"vin":"EDF226K7LZTZ51222","speed":39,"odometer":68234,"soc":87,"elevation":4737,"heading":33,"accuracy":24,"power":97,"shift_state":"D","range":64,"est_battery_range":307,"gps_as_of":1681704127537,"location":{"latitude":"83.3494","longitude":"141.9851"},"timestamp":1681704127537}
```

If you want to manually start the project, you can follow the steps below.

## Prerequisites

| Name      | Version | Description                                                                      |
| --------- | ------- | -------------------------------------------------------------------------------- |
| [EMQX Enterprise](https://www.emqx.com/en/products/emqx)      | 5.0.3+  | MQTT broker used for message exchange between MQTT clients and the Kafka system. |
| [Kafka](https://kafka.apache.org/)     | 2.8.0+  | Distributed streaming platform for collecting, storing, and processing data.     |
| [MQTTX CLI](https://mqttx.app/cli) | 1.9.3+  | Command-line tool used to generate simulated data for testing the system.        |
| [Grafana](https://grafana.com/)   | 9.5.1+  | Visualization platform utilized to display and analyze the collected data.       |

Note: In this project we refer to [kafka-docker](https://github.com/wurstmeister/kafka-docker)

## License

[Apache License 2.0](./LICENSE)
