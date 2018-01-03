package daos

import models.Img
import org.slf4j.LoggerFactory

import scala.concurrent.Future

object DAOImg {
  import psql._

  import concurrent.ExecutionContext.Implicits.global

  private val log = LoggerFactory.getLogger("img")

  def insert(img: Img): Future[Int] = {
    val q = quote(query[Img].insert(lift(img)))
    psql.run(q)
      .map { i =>
        log.info(s"New image is inserted into db: ${img.filename}.")
        i.toInt
      }.recoverWith {
        case e =>
          val m = s"Error while inserting image `${img.filename}`: ${e.getMessage}!"
          log.error(m)
          Future.failed(new Exception(m))
      }
  }

  def update(filename: String, bytes: Array[Byte]): Future[Int] = {
    val q = quote(query[Img].filter(_.filename == lift(filename)).update(_.bytes -> lift(bytes)))
    psql.run(q).map { result =>
      log.info(s"Filename $filename was updated.")
      result.toInt
    }
  }

  def upsert(image: Img) = {
    get(image.filename).flatMap {
      case None => insert(image)
      case Some(img) => update(img.filename, image.bytes)
    }
  }

  def remove(filename: String) = {
    val q = quote(query[Img].filter(_.filename == lift(filename)).delete)
    psql.run(q).map { result =>
      log.info(s"Filename $filename was deleted.")
      result.toInt
    }
  }

  def get(filename: String): Future[Option[Img]] = {
    val q = quote(query[Img].filter(_.filename == lift(filename)))
    psql.run(q).map(_.headOption)
  }

  def searchImg(filename: Option[String]): Future[List[Img]] = {
    val likeFilename = filename.getOrElse("%")
    val q = quote(query[Img].filter(_.filename like lift(likeFilename)))
    psql.run(q)
  }

  def search(filename: Option[String]): Future[List[String]] = {
    val likeFilename = filename.getOrElse("%")
    val q = quote(query[Img].filter(_.filename like lift(likeFilename)).map(_.filename))
    psql.run(q)
  }

}
