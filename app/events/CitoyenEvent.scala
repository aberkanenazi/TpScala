package events

import models.Citoyen
import net.liftweb.json.Serialization.write
import net.liftweb.json._


case class Event(name: String, citoyen_id: String, payload: String)

class CitoyenEvent {


  def createdCitoyenEvent(citoyen: Citoyen): String = {
    implicit val formats = DefaultFormats
    val payload = Map("matricule" -> citoyen.matricule, "name" -> citoyen.name, "gender" -> citoyen.gender, "age" -> citoyen.age.toString(), "state" -> citoyen.state)
    val e = Event("Citoyen Created", citoyen.matricule, write(payload))
    return write(e)
  }

  def updatedCitoyenEvent(citoyen: Citoyen, context: Citoyen, command: String): String = {
    implicit val formats = DefaultFormats

    val originalSet = Map("name" -> context.name, "gender" -> context.gender, "age" -> context.age.toString(), "state" -> context.state)
    val modifiedSet = Map("name" -> citoyen.name, "gender" -> citoyen.gender, "age" -> citoyen.age.toString(), "state" -> citoyen.state)

    val jsonPayload = write((modifiedSet.toSet diff originalSet.toSet).toMap)
    val e = Event(command, citoyen.matricule, jsonPayload)
    return write(e)
  }

  def updatedCitoyenNoExistEvent(citoyen: Citoyen): String = {
    implicit val formats = DefaultFormats
    val payload = Map("matricule" -> citoyen.matricule, "name" -> citoyen.name, "gender" -> citoyen.gender, "age" -> citoyen.age.toString(), "state" -> citoyen.state)
    val e = Event("Citoyen Not Exist", citoyen.matricule, write(payload))
    return write(e)
  }

  def createdCitoyenAlreadyExistEvent(citoyen: Citoyen): String = {
    implicit val formats = DefaultFormats
    val payload = Map("matricule" -> citoyen.matricule, "name" -> citoyen.name, "gender" -> citoyen.gender, "age" -> citoyen.age.toString(), "state" -> citoyen.state)
    val e = Event("Citoyen Already Exist", citoyen.matricule, write(payload))
    return write(e)
  }

  def updateStateNonAuthorized(citoyen :Citoyen) : String ={
    implicit val formats = DefaultFormats
    val payload = Map("matricule" -> citoyen.matricule, "name" -> citoyen.name, "gender" -> citoyen.gender, "age" -> citoyen.age.toString(), "state" -> citoyen.state)
    val e = Event("Update Non Authorized", citoyen.matricule, write(payload))
    return write(e)
  }
}
