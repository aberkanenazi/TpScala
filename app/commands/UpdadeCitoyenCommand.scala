package commands

import events.CitoyenEvent
import javax.inject.Inject
import kafka.KafkaProducerCitoyen
import models.Citoyen
import services.CitoyenServiceImpl
import scala.concurrent.ExecutionContext

class UpdadeCitoyenCommand @Inject()(implicit ec: ExecutionContext, citoyenServiceImpl: CitoyenServiceImpl) extends Command[Citoyen] {
  val citoyenEvent = new CitoyenEvent

  override def execute(citoyen: Citoyen): Citoyen = {

    val c = citoyen.setState("citoyen")
    citoyenServiceImpl.find(citoyen.matricule).map {
      case Some(citoyen) => KafkaProducerCitoyen.sendToKafka(citoyenEvent.createdCitoyenAlreadyExistEvent(citoyen))
      case _ => citoyenServiceImpl.save(c).map {
        _ => KafkaProducerCitoyen.sendToKafka(citoyenEvent.createdCitoyenEvent(c))
      }
    }
    return c
  }
}
