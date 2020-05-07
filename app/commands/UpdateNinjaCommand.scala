package commands


import events.CitoyenEvent
import javax.inject.Inject
import kafka.KafkaProducerCitoyen
import models.Citoyen
import services.CitoyenServiceImpl

import scala.concurrent.ExecutionContext

class UpdateNinjaCommand @Inject()(implicit ec: ExecutionContext, citoyenServiceImpl: CitoyenServiceImpl) extends Command[Citoyen] {
  val citoyenEvent = new CitoyenEvent

  override def execute(modifiedCitoyen: Citoyen): Citoyen = {

    citoyenServiceImpl.find(modifiedCitoyen.matricule).map {
      case Some(citoyen) => citoyenServiceImpl.update(modifiedCitoyen.matricule, modifiedCitoyen).map {
        _ => KafkaProducerCitoyen.sendToKafka(citoyenEvent.updatedCitoyenEvent(modifiedCitoyen, citoyen, "UpdatedNinja"))
      }
      case _ => KafkaProducerCitoyen.sendToKafka(citoyenEvent.createdCitoyenNoExistEvent(modifiedCitoyen))
    }

    return modifiedCitoyen
  }


}
