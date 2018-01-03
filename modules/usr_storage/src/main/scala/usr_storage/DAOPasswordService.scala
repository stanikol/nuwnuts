package usr_storage

import javax.inject.Singleton

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import usr_storage.model.{ Credential, LoginInfoDb, PasswordInfoDb }

import scala.collection.mutable
import scala.concurrent.Future

@Singleton
class DAOPasswordService extends DelegableAuthInfoDAO[PasswordInfo] {
  import concurrent.ExecutionContext.Implicits.global
  import psql._

  /**
   * The data store for the auth info.
   */
  var data: mutable.HashMap[LoginInfo, PasswordInfo] = mutable.HashMap()

  /**
   * Finds the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    val LoginInfo(pid, pk) = loginInfo
    val q = quote(query[Credential].filter { c =>
      c.loginInfoDb.providerId == lift(pid) && c.loginInfoDb.providerKey == lift(pk)
    })
    psql.run(q).map(_.headOption.map { c =>
      PasswordInfo(c.passwordInfoDb.hasher, c.passwordInfoDb.password, c.passwordInfoDb.salt)
    })
    //    println(s"He => $loginInfo")
    //    Future.successful(data.get(loginInfo))
  }

  /**
   * Adds new auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be added.
   * @param authInfo The auth info to add.
   * @return The added auth info.
   */
  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    val credential = Credential(LoginInfoDb(loginInfo), PasswordInfoDb(authInfo))
    val q = quote(query[Credential].insert(lift(credential)))
    psql.run(q).map(_ => authInfo)
    //    data += (loginInfo -> authInfo)
    //    Future.successful(authInfo)
  }

  /**
   * Updates the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be updated.
   * @param authInfo The auth info to update.
   * @return The updated auth info.
   */
  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    val credential = Credential(LoginInfoDb(loginInfo), PasswordInfoDb(authInfo))
    val LoginInfo(providerID, providerKey) = loginInfo
    val q = quote(query[Credential].filter { c =>
      c.loginInfoDb.providerId == lift(providerID) && c.loginInfoDb.providerKey == lift(providerKey)
    }.update(lift(credential)))
    psql.run(q).map(_ => authInfo)
    //    data += (loginInfo -> authInfo)
    //    Future.successful(authInfo)
  }

  /**
   * Saves the auth info for the given login info.
   *
   * This method either adds the auth info if it doesn't exists or it updates the auth info
   * if it already exists.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The auth info to save.
   * @return The saved auth info.
   */
  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)
      case None => add(loginInfo, authInfo)
    }
  }

  /**
   * Removes the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be removed.
   * @return A future to wait for the process to be completed.
   */
  def remove(loginInfo: LoginInfo): Future[Unit] = {
    val LoginInfo(pid, pk) = loginInfo
    val q = quote(query[Credential].filter { c =>
      c.loginInfoDb.providerId == lift(pid) && c.loginInfoDb.providerKey == lift(pk)
    }.delete)
    psql.run(q).map(_ => ())
    //    data -= loginInfo
    //    Future.successful(())
  }
}