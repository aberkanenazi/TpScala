package kafka

import java.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import models.Citoyen
import org.apache.kafka.common.serialization.Serializer

class CustomSerializer extends Serializer[Citoyen] {

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def serialize(topic: String, data: Citoyen): Array[Byte] = {
    try {
      val mapper = new ObjectMapper()
      mapper.registerModule(DefaultScalaModule)
      println(mapper.writeValueAsString(data))
      val result = mapper.writeValueAsString(data).getBytes()
      result
    } catch {
      case ex: JsonProcessingException => throw new IllegalArgumentException(ex);
    }
  }

  override def close(): Unit = {}
}
