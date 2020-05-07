package repositories

import models.Citoyen
import reactivemongo.api.commands.WriteResult
import scala.concurrent.{ExecutionContext, Future}

trait CitoyenRepository {

  def findAll(limit: Int)(implicit ec: ExecutionContext): Future[Seq[Citoyen]]

  def findByMatricule(matricule: String)(implicit ec: ExecutionContext): Future[Option[Citoyen]]

  def save(citoyen: Citoyen)(implicit ec: ExecutionContext): Future[WriteResult]

  def update(matricule: String, citoyen: Citoyen)(implicit ec: ExecutionContext): Future[Option[Citoyen]]

  def remove(matricule: String)(implicit ec: ExecutionContext): Future[Option[Citoyen]]
}
