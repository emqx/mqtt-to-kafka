import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class MqttKafkaBridge implements MqttCallback {

  private MqttClient mqttClient;
  private KafkaProducer<String, String> kafkaProducer;

  public MqttKafkaBridge() {
  }

  public void start() throws MqttException {

    // 加载配置文件
    Properties properties = loadProperties();

    // 初始化 MQTT 客户端
    String broker = "tcp://" + properties.getProperty("mqtt.host") + ":" + properties.getProperty("mqtt.port");
    // 如果配置文件中没有 clientid,则使用随机生成的 clientid
    String clientId = properties.getProperty("mqtt.clientid");
    if (clientId == null || clientId.isEmpty()) {
      clientId = MqttClient.generateClientId();
    }
    MemoryPersistence persistence = new MemoryPersistence();
    mqttClient = new MqttClient(broker, clientId, persistence);
    mqttClient.setCallback(this);

    // 设置 MQTT 连接参数
    MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setUserName(properties.getProperty("mqtt.username"));
    connOpts.setPassword(properties.getProperty("mqtt.password").toCharArray());
    connOpts.setCleanSession(Boolean.parseBoolean(properties.getProperty("mqtt.clean_session")));
    connOpts.setKeepAliveInterval(Integer.parseInt(properties.getProperty("mqtt.keepalive")));
    if (Boolean.parseBoolean(properties.getProperty("mqtt.tls"))) {
      // 设置 TLS 参数
      // connOpts.setSocketFactory(...);
    }

    // 连接 MQTT Broker
    mqttClient.connect(connOpts);

    // 订阅 MQTT 主题
    String[] topics = properties.getProperty("mqtt.topics").split(",");
    int[] qos = new int[topics.length];
    for (int i = 0; i < qos.length; i++) {
      qos[i] = 0;
    }
    if (Boolean.parseBoolean(properties.getProperty("mqtt.shared_subscription"))) {
      for (int i = 0; i < topics.length; i++) {
        topics[i] = "$share/1/" + topics[i];
      }
    }
    mqttClient.subscribe(topics, qos);

    // 初始化 Kafka 生产者
    Properties kafkaProps = new Properties();
    kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty("kafka.bootstrap_servers"));
    kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    kafkaProducer = new KafkaProducer<>(kafkaProps);
  }

  @Override
  public void connectionLost(Throwable throwable) {
  }

  @Override
  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
    // 处理 MQTT 消息
    String processedData = processMessage(mqttMessage.toString());

    // 发送消息到 Kafka
    ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic, processedData);
    kafkaProducer.send(record);
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
  }

  private Properties loadProperties() {
    // 从配置文件中读取参数
  }

  private String processMessage(String message) {
    // 处理消息的方法
    return message;
  }

  public static void main(String[] args) throws MqttException {
    MqttKafkaBridge app = new MqttKafkaBridge();
    app.start();
  }
}