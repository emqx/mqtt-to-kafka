const mqtt = require('mqtt'); // 引入 MQTT 库
const { Kafka } = require('kafkajs'); // 引入 Kafka 库
const config = require('./config.json'); // 引入配置文件

// 创建 MQTT 客户端并连接到 MQTT Broker
const mqttClient = mqtt.connect(`mqtt://${config.mqtt.host}:${config.mqtt.port}`, {
  username: config.mqtt.username,
  password: config.mqtt.password,
  clientId: config.mqtt.clientId,
  keepalive: config.mqtt.keepalive,
  clean: config.mqtt.cleanStart,
  rejectUnauthorized: false, // 允许连接不受信任的 MQTT 服务器
});

// 创建 Kafka 客户端并连接到 Kafka 集群
const kafka = new Kafka({
  clientId: 'my-app',
  brokers: config.kafka.bootstrapServers,
});

// 创建 Kafka 生产者
const producer = kafka.producer();

// 等待 Kafka 生产者连接到 Kafka 集群
async function waitKafkaConnect() {
  await producer.connect();
  console.log('Kafka producer connected.');
}
waitKafkaConnect();

// 订阅 MQTT 主题
// 如果启用了共享订阅，则为所有主题加上 $share/1 前缀
const topics = config.mqtt.topics.map(topic => config.mqtt.sharedSubscription ? `$share/1/${topic}` : topic);
mqttClient.on('connect', () => {
  mqttClient.subscribe(topics, (err) => {
    if (err) {
      console.error('Failed to subscribe:', err);
    } else {
      console.log(`Subscribed to MQTT topics: ${topics.join(', ')}`);
    }
  });
});

// TODO: 在这里对消息进行处理，处理结果写入 Kafka
function processMessage(message) {
  return message;
}

// 处理 MQTT 消息并将处理结果写入 Kafka 主题
mqttClient.on('message', async (topic, message) => {
  console.log(`Received MQTT message from ${topic}`);

  const processedMessage = processMessage(message)
  await producer.send({
    topic: config.kafka.topic,
    messages: [
      { 
        // key: topic,
        value: processedMessage,
      },
    ],
  });
  console.log(`Stream ${topic} message to Kafka topic ${config.kafka.topic} successfully`);
});



// 处理错误
mqttClient.on('error', (err) => {
  console.error('MQTT client error:', err);
});

producer.on('producer.disconnect', (err) => {
  console.error('Kafka producer error:', err);
});
