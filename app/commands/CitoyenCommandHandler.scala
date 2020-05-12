package commands

import events.CitoyenEvent
import javax.inject.Inject
import kafka.KafkaProducerCitoyen
import models.Citoyen
import services.CitoyenServiceImpl

import scala.concurrent.ExecutionContext
import scala.util.Random

class CitoyenCommandHandler @Inject()(implicit ec: ExecutionContext, citoyenServiceImpl: CitoyenServiceImpl) {
  val citoyenEvent = new CitoyenEvent

  /**
   * insert a citoyen with a default state citoyen
   * @param citoyen
   * @return
   */
  def insert(citoyen: Citoyen): Citoyen = {
    val c = citoyen.setState("citoyen")
    citoyenServiceImpl.find(citoyen.matricule).map {
      case Some(citoyen) => KafkaProducerCitoyen.sendToKafka(citoyenEvent.createdCitoyenAlreadyExistEvent(citoyen))
      case _ => citoyenServiceImpl.save(c).map {
        _ => KafkaProducerCitoyen.sendToKafka(citoyenEvent.createdCitoyenEvent(c))
      }
    }
    return citoyen
  }

  /**
   * Update the state of a citoyen
   *
   * @param modifiedCitoyen
   * @return
   */
  def update(modifiedCitoyen: Citoyen): Citoyen= {
    citoyenServiceImpl.find(modifiedCitoyen.matricule).map {
      case Some(citoyen) =>
        citoyen.state match {
          case "ninja"=> if(modifiedCitoyen.state.equals("genin")) {
              citoyenServiceImpl.update(modifiedCitoyen.matricule, modifiedCitoyen).map { _ => KafkaProducerCitoyen.sendToKafka(citoyenEvent.updatedCitoyenEvent(modifiedCitoyen, citoyen, "Citoyen Updated To Genin"))}
          }else{
            KafkaProducerCitoyen.sendToKafka(citoyenEvent.updateStateNonAuthorized(modifiedCitoyen))
          }
          case "citoyen"=> if(modifiedCitoyen.state.equals("ninja") || modifiedCitoyen.state.equals("habitant")) {
              val message = "Citoyen Updated To "+modifiedCitoyen.state
              citoyenServiceImpl.update(modifiedCitoyen.matricule, modifiedCitoyen).map { _ => KafkaProducerCitoyen.sendToKafka(citoyenEvent.updatedCitoyenEvent(modifiedCitoyen, citoyen, message))}
          }else{
              KafkaProducerCitoyen.sendToKafka(citoyenEvent.updateStateNonAuthorized(modifiedCitoyen))
          }
          case _=> KafkaProducerCitoyen.sendToKafka(citoyenEvent.updateStateNonAuthorized(modifiedCitoyen))
        }
      case _ => KafkaProducerCitoyen.sendToKafka(citoyenEvent.updatedCitoyenNoExistEvent(modifiedCitoyen))
    }
   modifiedCitoyen
  }


}
