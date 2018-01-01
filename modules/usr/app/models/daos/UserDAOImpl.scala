package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import daos.model.{ LoginInfoDb, UserDb }
import models.User
import models.daos.UserDAOImpl._

import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO {
  import concurrent.ExecutionContext.Implicits.global

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo): Future[Option[User]] =
    daos.DAOUserDb.find(LoginInfoDb(loginInfo.providerID, loginInfo.providerKey)).map(_.map(fromUsr))

  //    Future.successful(
  //    users.find { case (_, user) => user.loginInfo == loginInfo }.map(_._2)
  //  )

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userID: UUID): Future[Option[User]] =
    daos.DAOUserDb.find(userID).map(_.map(fromUsr))
  //    Future.successful(users.get(userID))

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = {
    //    users += (user.userID -> user)
    saveToDb(user).map { _ => user }
  }

  def saveToDb(user: User) = {
    val usr = getUsr(user)
    daos.DAOUserDb.upsert(usr)
  }
}

/**
 * The companion object.
 */
object UserDAOImpl {

  /**
   * The list of users.
   */
  //  val users: mutable.HashMap[UUID, User] = mutable.HashMap()

  def getLogInfo(user: User): LoginInfoDb = {
    daos.model.LoginInfoDb(user.loginInfo.providerID, user.loginInfo.providerKey)
  }

  def getUsr(user: User): UserDb = {
    val logInfo: LoginInfoDb = getLogInfo(user)
    daos.model.UserDb(user.userID, logInfo, user.firstName, user.lastName, user.fullName,
      user.email, user.avatarURL, user.activated, user.roles)
  }

  def fromUsr(usr: UserDb): User = {
    val loginInfo: LoginInfo = LoginInfo(usr.loginInfo.providerId, usr.loginInfo.providerKey)
    User(usr.userId, loginInfo, usr.firstName, usr.lastName, usr.fullName, usr.email, usr.avatarUrl, usr.activated, usr.roles)
  }
}
