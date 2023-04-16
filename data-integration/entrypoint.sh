#!/bin/sh

$api_secret=$(echo uuidgen)

echo "myapi:$api_secret" > bootstrap_api_key.conf

echo "Create data integration and rule"

curl 'http://localhost:18083/api/v5/bridges' \
  -u "myapi:$api_secret" \
  -H 'Content-Type: application/json' \
  --data-raw '{"type":"kafka","name":"my-kafka","bootstrap_hosts":"localhost:9092","connect_timeout":"5s","min_metadata_refresh_interval":"3s","metadata_request_timeout":"5s","authentication":"none","socket_opts":{"sndbuf":"1024KB","recbuf":"1024KB"},"ssl":{"enable":false,"verify":"verify_peer"},"kafka":{"topic":"my-kafka-topic","message":{"key":"${.clientid}","value":"${payload}","timestamp":"${.timestamp}"},"max_batch_bytes":"896KB","compression":"no_compression","partition_strategy":"random","required_acks":"all_isr","partition_count_refresh_interval":"60s","max_inflight":10,"buffer":{"mode":"memory","per_partition_limit":"2GB","segment_bytes":"100MB","memory_overload_protection":false}}}'

curl 'http://localhost:18083/api/v5/rules' \
  -u "myapi:$api_secret" \
  -H 'Content-Type: application/json' \
  --data-raw '{"id":"rule_aoj3","sql":"SELECT\n  *\nFROM\n  \"iot_simulator/#\", \"t/#\"","actions":["kafka:my-kafka"],"description":""}'
