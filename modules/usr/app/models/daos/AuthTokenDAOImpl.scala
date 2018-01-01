package models.daos

import java.time.LocalDateTime
import java.util.UUID

import daos.model.AuthTokenDb
import models.AuthToken
import models.daos.AuthTokenDAOImpl._
import org.joda.time.DateTime

import scala.collection.mutable
import scala.concurrent.Future

/**
 * Give access to the [[AuthToken]] object.
 */
class AuthTokenDAOImpl extends AuthTokenDAO {
  import concurrent.ExecutionContext.Implicits.global

  /**
   * Finds a token by its ID.
   *
   * @param id The unique token ID.
   * @return The found token or None if no token for the given ID could be found.
   */
  def find(id: UUID): Future[Option[AuthToken]] = //Future.successful(tokens.get(id))
    daos.DAOAuthTokenDb.find(id).map(_.map(fromAuthTokenDb))

  /**
   * Finds expired tokens.
   *
   * @param dateTime The current date time.
   */
  def findExpired(dateTime: LocalDateTime): Future[Seq[AuthToken]] = daos.DAOAuthTokenDb.findExpired(dateTime).map(_.map(fromAuthTokenDb))
  //    Future.successful {
  //    tokens.filter {
  //      case (_, token) =>
  //        token.expiry.isBefore(dateTime)
  //    }.values.toSeq
  //  }

  /**
   * Saves a token.
   *
   * @param token The token to save.
   * @return The saved token.
   */
  def save(token: AuthToken): Future[AuthToken] =
    daos.DAOAuthTokenDb.save(AuthTokenDb(token.id, token.userID, token.expiry)).map(fromAuthTokenDb)
  //  {
  //    tokens += (token.id -> token)
  //    Future.successful(token)
  //  }

  /**
   * Removes the token for the given ID.
   *
   * @param id The ID for which the token should be removed.
   * @return A future to wait for the process to be completed.
   */
  def remove(id: UUID): Future[Unit] =
    daos.DAOAuthTokenDb.remove(id)
  //  {
  //    tokens -= id
  //    Future.successful(())
  //  }
}

/**
 * The companion object.
 */
object AuthTokenDAOImpl {

  /**
   * The list of tokens.
   */
  val tokens: mutable.HashMap[UUID, AuthToken] = mutable.HashMap()

  def fromAuthTokenDb(token: AuthTokenDb): AuthToken = AuthToken(token.id, token.userId, token.expiry)
}
