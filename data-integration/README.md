# MQTT to Kafka - Data Integration

Use out-of-the-box data integration provided by EMQX to streaming message into Confluent/Kafka without writing code.

## Prerequisites

- EMQX: 5.0.0+
- cURL

## Create Rule and Data Integration

Use EMQX REST to create a rule and data integration.

You can refer to [EMQX - Stream Data into Apache Kafka](https://docs.emqx.com/en/enterprise/v5.0/data-integration/data-bridge-kafka.html) for more details. It is a better way to create a data bridge.

<!-- TODO EMQX Dashboard supports export data bridge as JSON for init config.-->

To make the tutorial quick, here is the creation using the REST API.

1. Create API Key:

Please refer to [EMQX - REST API Authentication](https://docs.emqx.com/en/enterprise/v5.0/admin/api.html#authentication) for more details.

Now, you can use the API Key to access the REST API.

2. Create a Kafka Data Integration:

Major configurations:

- name: `my-kafka`
- bootstrap_hosts: `localhost:9092`
- kafka topic: `my-kafka-topic`
- message: `{"key":"${.clientid}", "value":"${payload}", "timestamp":"${.timestamp}"}`

cURL Request:

```bash
curl 'http://localhost:18083/api/v5/bridges' \
  -u 'myapi:<generated secret key>' \
  -H 'Content-Type: application/json' \
  --data-raw '{"type":"kafka","name":"my-kafka","bootstrap_hosts":"localhost:9092","connect_timeout":"5s","min_metadata_refresh_interval":"3s","metadata_request_timeout":"5s","authentication":"none","socket_opts":{"sndbuf":"1024KB","recbuf":"1024KB"},"ssl":{"enable":false,"verify":"verify_peer"},"kafka":{"topic":"my-kafka-topic","message":{"key":"${.clientid}","value":"${payload}","timestamp":"${.timestamp}"},"max_batch_bytes":"896KB","compression":"no_compression","partition_strategy":"random","required_acks":"all_isr","partition_count_refresh_interval":"60s","max_inflight":10,"buffer":{"mode":"memory","per_partition_limit":"2GB","segment_bytes":"100MB","memory_overload_protection":false}}}'
```

3. Create a Rule:

Major configurations:

- SQL: `SELECT * FROM "iot_simulator/#", "t/#"`
- Action: Data Integration `my-kafka`

cURL Request:

```bash
curl 'http://localhost:18083/api/v5/rules' \
  -u 'myapi:<generated secret key>' \
  -H 'Content-Type: application/json' \
  --data-raw '{"id":"rule_aoj3","sql":"SELECT\n  *\nFROM\n  \"iot_simulator/#\", \"t/#\"","actions":["kafka:my-kafka"],"description":""}'
```
