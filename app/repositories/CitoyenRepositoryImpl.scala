package repositories

import models.Citoyen
import javax.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class CitoyenRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi) extends CitoyenRepository {

  override def findAll(limit: Int = 100)(implicit ec: ExecutionContext): Future[Seq[Citoyen]] =
    collection.flatMap(_.find(BSONDocument()).cursor[Citoyen](ReadPreference.primary).collect[Seq](limit, Cursor.FailOnError[Seq[Citoyen]]()))

  override def findByMatricule(matricule: String)(implicit ec: ExecutionContext): Future[Option[Citoyen]] =
    collection.flatMap(_.find(BSONDocument("matricule" -> matricule)).one[Citoyen])

  override def save(citoyen: Citoyen)(implicit ec: ExecutionContext): Future[WriteResult] =
    collection.flatMap(_.insert(citoyen))

  private def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("dbVillages"))

  override def update(matricule: String, citoyen: Citoyen)(implicit ec: ExecutionContext): Future[Option[Citoyen]] =
    collection.flatMap(_.findAndUpdate(BSONDocument("matricule" -> matricule), BSONDocument(
      f"$$set" -> BSONDocument(
        "matricule" -> citoyen.matricule,
        "name" -> citoyen.name,
        "gender" -> citoyen.gender,
        "age" -> citoyen.age,
        "state" -> citoyen.state,
      )
    ),
      true
    ).map(_.result[Citoyen])
    )

  override def remove(matricule: String)(implicit ec: ExecutionContext): Future[Option[Citoyen]] =
    collection.flatMap(_.findAndRemove(BSONDocument("matricule" -> matricule)).map(_.result[Citoyen]))
}
