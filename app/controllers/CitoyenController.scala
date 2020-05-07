package controllers

import commands.{UpdadeCitoyenCommand, UpdateNinjaCommand}
import javax.inject.Inject
import models.Citoyen
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import services.CitoyenServiceImpl

import scala.concurrent.{ExecutionContext, Future}

class CitoyenController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, citoyenServiceImpl: CitoyenServiceImpl, updateNinja: UpdateNinjaCommand, updateCitoyen: UpdadeCitoyenCommand) extends AbstractController(cc) {

  def index = Action.async {
    citoyenServiceImpl.findAll().map(citoyens => Ok(Json.toJson(citoyens)))
  }

  def read(matricule: String) = Action.async {
    citoyenServiceImpl.find(matricule).map { citoyen =>
      citoyen.map { citoyen =>
        Ok(Json.toJson(citoyen))
      }.getOrElse(NotFound("NOT_FOUND"))
    }
  }

  def create = Action.async(parse.json) {
    _.body.validate[Citoyen]
      .map {
        citoyen =>
          updateCitoyen.execute(citoyen)
          Future.successful(Ok(Json.toJson(citoyen)))
      }.getOrElse(Future.successful(BadRequest("INVALID_FORMAT")))
  }

  def update(matricule: String) = Action.async(parse.json) {
    _.body.validate[Citoyen].map {
      citoyen =>
        citoyen.state match {
          case "ninja" => updateNinja.execute(citoyen)
          //case "hokage" =>
        }
        Future.successful(Ok(Json.toJson(citoyen)))
    }.getOrElse(Future.successful(BadRequest("INVALID_FORMAT")))
  }

  def remove(matricule: String) = Action.async {
    citoyenServiceImpl.remove(matricule).map {
      case Some(citoyen) => Ok(Json.toJson(citoyen))
      case _ => NotFound("NOT_FOUND")
    }
  }
}