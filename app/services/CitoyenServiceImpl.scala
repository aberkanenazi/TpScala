package services

import javax.inject.Inject
import models.Citoyen
import reactivemongo.api.commands.WriteResult
import repositories.CitoyenRepositoryImpl

import scala.concurrent.{ExecutionContext, Future}

class CitoyenServiceImpl @Inject()(implicit ec: ExecutionContext, CitoyenRepository: CitoyenRepositoryImpl) {

  def findAll(): Future[Seq[Citoyen]] =
    CitoyenRepository.findAll()

  def find(matricule: String): Future[Option[Citoyen]] =
    CitoyenRepository.findByMatricule(matricule = matricule)

  def save(citoyen: Citoyen): Future[WriteResult] = {
    CitoyenRepository.save(citoyen)
  }

  def update(matricule: String, citoyen: Citoyen): Future[Option[Citoyen]] =
    CitoyenRepository.update(matricule = matricule, citoyen = citoyen)

  def remove(matricule: String): Future[Option[Citoyen]] =
    CitoyenRepository.remove(matricule = matricule)
}
