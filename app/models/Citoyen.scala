package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes, _}
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._

case class Citoyen(
                   _id: Option[BSONObjectID],
                   matricule: String,
                   name: String,
                   gender: String,
                   age: Int,
                   state: String,
                 ) {

  def setState(state: String) = new Citoyen(_id, matricule, name, gender, age, state)
}

object Citoyen {

  val citoyenWrites: Writes[Citoyen] = (
    (JsPath \ "_id").writeNullable[BSONObjectID] and
      (JsPath \ "matricule").write[String] and
      (JsPath \ "name").write[String] and
      (JsPath \ "gender").write[String] and
      (JsPath \ "age").write[Int] and
      (JsPath \ "state").write[String]
    ) (unlift(Citoyen.unapply))

  implicit val citoyenReads: Reads[Citoyen] = (
    (JsPath \ "_id").readNullable[BSONObjectID] and
      (JsPath \ "matricule").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "gender").read[String] and
      (JsPath \ "age").read[Int] and
      (JsPath \ "state").read[String]
    ) (Citoyen.apply _)

  implicit val citoyenFormat: Format[Citoyen] = Format(citoyenReads, citoyenWrites)

  implicit val citoyenOFormat = new OFormat[Citoyen] {
    override def reads(json: JsValue): JsResult[Citoyen] = citoyenFormat.reads(json)

    override def writes(o: Citoyen): JsObject = citoyenFormat.writes(o).asInstanceOf[JsObject]
  }
}