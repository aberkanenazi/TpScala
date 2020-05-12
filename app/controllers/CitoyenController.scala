package controllers

import commands.{CitoyenCommandHandler}
import javax.inject.Inject
import models.Citoyen
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import services.CitoyenServiceImpl

import scala.concurrent.{ExecutionContext, Future}

class CitoyenController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, citoyenServiceImpl: CitoyenServiceImpl, eventhandler: CitoyenCommandHandler) extends AbstractController(cc) {

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
          eventhandler.insert(citoyen)
          Future.successful(Ok(Json.toJson(citoyen)))
      }.getOrElse(Future.successful(BadRequest("INVALID_FORMAT")))
  }

  def update(matricule: String) = Action.async(parse.json) {
    _.body.validate[Citoyen].map {
      citoyen =>
        eventhandler.update(citoyen)
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