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
  implicit val citoyenFormat: Format[Citoyen]= {
    Json.format[Citoyen]
  }
  implicit val citoyenRead: Reads[Seq[Citoyen]]= Reads.seq(citoyenFormat)
  implicit val citoyenOFormat = new OFormat[Citoyen] {
    override def reads(json: JsValue): JsResult[Citoyen] = citoyenFormat.reads(json)

    override def writes(o: Citoyen): JsObject = citoyenFormat.writes(o).asInstanceOf[JsObject]
  }
}