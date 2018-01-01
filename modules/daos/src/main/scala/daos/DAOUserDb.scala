package daos

import java.util.UUID

import model.{ LoginInfoDb, UserDb }

import scala.concurrent.ExecutionContext
//import play.api.daos

import scala.concurrent.Future

object DAOUserDb {

  import concurrent.ExecutionContext.Implicits.global
  import psql._

  def listAll = {
    val q = quote(query[UserDb])
    psql.run(q)
  }

  def insert(u: UserDb): Future[Long] = {
    val q = quote(query[UserDb].insert(lift(u)))
    psql.run(q)
  }

  def update(u: UserDb): Future[Long] = {
    val q = quote(query[UserDb].filter(_.userId == lift(u.userId)).update(lift(u)))
    psql.run(q)
  }

  def find(userId: UUID): Future[Option[UserDb]] = {
    val q = quote(query[UserDb].filter(_.userId == lift(userId)))
    psql.run(q).map(_.headOption)
  }

  def find(logInfo: LoginInfoDb): Future[Option[UserDb]] = {
    val q = quote(query[UserDb].filter { u =>
      u.loginInfo.providerKey == lift(logInfo.providerKey) &&
        u.loginInfo.providerId == lift(logInfo.providerId)
    })
    psql.run(q).map(_.headOption)
  }

  def find(u: UserDb): Future[Option[UserDb]] = find(u.userId)

  def upsert(u: UserDb): Future[Long] = {
    find(u).flatMap {
      case None => insert(u)
      case Some(_) => update(u)
    }
  }
  //  def findByEmail(email: String): Future[Option[UserDb]] = {
  //    daos.run(quote(query[UserDb].filter(_.email == lift(email)))).map(_.headOption)
  //  }
  //
  //  def save(user: UserDb): Future[UserDb] = {
  //    findByEmail(user.email).flatMap {
  //      case None =>
  //        val q = quote(query[UserDb].insert(lift(user)).returning(_.id))
  //        daos.run(q).map(id => user.copy(id = id))
  //      case Some(usr) =>
  //        val q = quote(query[UserDb].filter(_.email == lift(usr.email)).update(lift(user)))
  //        daos.run(q).map(_ => user.copy(id = usr.id))
  //    }
  //  }
  //
  //  def remove(usr: UserDb): Future[Unit] = {
  //    findByEmail(usr.email).map {
  //      case Some(_) =>
  //        daos.run(quote(query[UserDb].filter(_.email == lift(usr.email)).delete))
  //      case None => Future.successful()
  //    }
  //  }

}
