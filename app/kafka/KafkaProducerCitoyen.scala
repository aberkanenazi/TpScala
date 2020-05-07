package kafka

import java.util.Properties

import org.apache.kafka.clients.producer._
import reactivemongo.bson.BSONObjectID

object KafkaProducerCitoyen {

  val bootstrapServer = "127.0.0.1:9092"
  val groupId = "citoyen-id"
  val topic = "citoyen-event"

  val props: Properties = {
    val prop = new Properties()
    prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
    prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    prop.put(ProducerConfig.ACKS_CONFIG, "all")
    prop
  }

  def sendToKafka(event: String): Unit = {

    val callback = new Callback {
      override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
        println("Callback" + metadata.toString)
      }
    }
    val producer = new KafkaProducer[String, String](props)

    producer.send(new ProducerRecord(topic, BSONObjectID.generate().stringify, event), callback)
    producer.close()
  }
}
