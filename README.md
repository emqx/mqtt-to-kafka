# mqtt-to-kafka

Streaming IoT data into Confluent/Kafka using MQTT and EMQX | MQTT Kafka Integration.

## Introduction

This is a demo project that shows how to use EMQX and Kafka to build a streaming data pipeline. The project uses MQTTX CLI to simulate the MQTT client to publish vehicle data to EMQX, and then uses EMQX Data Integration to streaming the data to Kafka.

## How to use

Please make sure you have installed the docker, and just run the `make` command to start all of the containers.

You can see the message from Kafka consumer:

 ```json
 {"vin":"EDF226K7LZTZ51222","speed":39,"odometer":68234,"soc":87,"elevation":4737,"heading":33,"accuracy":24,"power":97,"shift_state":"D","range":64,"est_battery_range":307,"gps_as_of":1681704127537,"location":{"latitude":"83.3494","longitude":"141.9851"},"timestamp":1681704127537}
 ```

## Scenario

Kafka and MQTT are complementary technologies that enable end-to-end integration of IoT data. By integrating Kafka and MQTT, businesses can establish a robust IoT architecture that guarantees reliable connectivity and efficient data exchange between devices and IoT platforms.

Some of the prominent use cases where this architecture is applicable include Telematics and Vehicle Data Analytics, Intelligent Traffic Management, Remote Diagnostics, Energy Efficiency, and Environmental Impact and Predictive Maintenance.

## Prerequisites

| Name      | Version | Description                                                                      |
| --------- | ------- | -------------------------------------------------------------------------------- |
| [EMQX Enterprise](https://www.emqx.com/en/products/emqx)      | 5.0.3+  | MQTT broker used for message exchange between MQTT clients and the Kafka system. |
| [EMQX Exporter](https://github.com/emqx/emqx-exporter)      | 0.1 | Prometheus exporter for EMQX |
| [MQTTX CLI](https://mqttx.app/cli) | 1.9.3+  | Command-line tool used to generate simulated data for testing.        |
| [Prometheus](https://prometheus.io/)   | v2.44.0  | Open-source systems monitoring and alerting toolkit.       |
| [Grafana](https://grafana.com/)   | 9.5.1+  | Visualization platform utilized to display and analyze the collected data.       |
| [Kafka](https://kafka.apache.org/)     | 2.8.0+  | Distributed streaming platform for collecting, storing, and processing data.     |

Note: In this project we refer to [kafka-docker](https://github.com/wurstmeister/kafka-docker).

## License

[Apache License 2.0](./LICENSE)
