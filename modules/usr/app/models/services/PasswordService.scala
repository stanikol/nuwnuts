package models.services

import com.google.inject.Singleton
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{ AuthInfo, LoginInfo }
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import play.api.data

import scala.collection.mutable
import scala.concurrent.Future
import scala.reflect.ClassTag

@Singleton
class PasswordService extends DelegableAuthInfoDAO[PasswordInfo] {
  import concurrent.ExecutionContext.Implicits.global
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
    println(s"He => $loginInfo")
    Future.successful(data.get(loginInfo))
  }

  /**
   * Adds new auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be added.
   * @param authInfo The auth info to add.
   * @return The added auth info.
   */
  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    data += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  /**
   * Updates the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be updated.
   * @param authInfo The auth info to update.
   * @return The updated auth info.
   */
  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    data += (loginInfo -> authInfo)
    Future.successful(authInfo)
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
    data -= loginInfo
    Future.successful(())
  }
}