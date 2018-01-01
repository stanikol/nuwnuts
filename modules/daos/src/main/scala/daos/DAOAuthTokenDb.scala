package daos

import java.time.LocalDateTime
import java.util.{ Date, UUID }

import io.getquill
import model.AuthTokenDb
import io.getquill._
import io.getquill.context.async.SqlTypes
import org.joda.time.DateTime

import scala.concurrent.Future

object DAOAuthTokenDb {
  import concurrent.ExecutionContext.Implicits.global
  import psql._

  //  implicit val decodeDateTime = MappedEncoding[String, DateTime](_ => DateTime.now())
  //  implicit val encodeDateTime = MappedEncoding[DateTime, String](_ => "2019-01-01T03:12:50.871+02:00")

  def find(id: UUID): Future[Option[AuthTokenDb]] = {
    val q = quote(query[AuthTokenDb].filter(_.id == lift(id)))
    psql.run(q).map(_.headOption)
  }

  def findExpired(dateTime: LocalDateTime): Future[Seq[AuthTokenDb]] = {
    //    val q = quote(query[AuthTokenDb].filter(_.expiry.isBefore(lift(dateTime)))) //TODO: Add filtering to query
    val q = quote(query[AuthTokenDb]) //TODO: Add filtering to query
    psql.run(q).map(_.filter(_.expiry.isBefore(dateTime)))
  }

  def save(token: AuthTokenDb): Future[AuthTokenDb] = {
    find(token.id).flatMap {
      case None =>
        val q = quote(query[AuthTokenDb].insert(lift(token)))
        psql.run(q)
      case Some(_) =>
        val q = quote(query[AuthTokenDb].filter(_.id == lift(token.id)).update(lift(token)))
        psql.run(q)
    }.map(_ => token)
  }

  def remove(id: UUID): Future[Unit] = {
    val q = quote(query[AuthTokenDb].filter(_.id == lift(id)).delete)
    psql.run(q).map(_ => ())
  }
}
