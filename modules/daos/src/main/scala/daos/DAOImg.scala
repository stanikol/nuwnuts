package daos

import java.nio.file.{ Files, Path, Paths }

import com.typesafe.config.Config
import model.Img
import collection.JavaConverters._
import scala.concurrent.{ ExecutionContext, Future }

object DAOImg {

  import psql._

  import concurrent.ExecutionContext.Implicits.global

  def find(filename: String): Future[Option[Img]] = {
    val q = quote(query[Img].filter(_.filename == lift(filename)))
    psql.run(q).map(_.headOption)
  }

  def remove(filename: String): Future[Unit] = {
    find(filename).map {
      case None => Future.failed(new Exception(s"Image $filename is not found !"))
      case Some(img) =>
        val q = quote(query[Img].filter(_.filename == lift(filename)).delete)
        psql.run(q)
    }
  }

  def upsert(img: Img): Future[Img] = {
    find(img.filename).flatMap {
      case None => insert(img)
      case Some(foundImg) =>
        val q = quote(query[Img].filter(_.filename == lift(img.filename)).update(lift(img.copy(id = foundImg.id))))
        psql.run(q).map(_ => img)
    }
  }

  def insert(img: Img): Future[Img] = {
    val q = quote(query[Img].insert(lift(img)).returning(_.id))
    psql.run(q).map(id => img.copy(id = id))
  }

  def insert(images: List[Img]): Future[List[Option[Long]]] = {
    val q = quote(
      liftQuery(images).foreach(img => query[Img].insert(img).returning(_.id))
    )
    psql.run(q)
  }

  /**
   * Uses config to import images from directories
   * Example config:
   * ```img{
   *   import-dirs = ["/Users/snc/Walnuts/oreh.od.ua/www/oreh.od.ua/assets/images", "/Users/snc/Walnuts/oreh.od.ua/www/oreh.od.ua/static/uploads"]
   *   extensions = [".jpg", ".jpeg", ".png"]
   * }```
   * @param config
   * @return
   */
  def importImages(config: Config) = {
    val supportedExtensions: List[String] = config.getStringList("img.extensions").asScala.toList
    val importFrom = config.getStringList("img.import-dirs").asScala.toList
    val resultF = for { importDir <- importFrom } yield {
      val imgFiles: Set[Path] = Files.walk(Paths.get(importDir)).iterator().asScala.toSet
        .filter(f => f.toFile.isFile && supportedExtensions.exists(f.toString.endsWith(_)))
      val images: List[Img] = imgFiles.map(imgPath => Img.fromPath(imgPath))
        .filter(_.isSuccess).map(_.get).toList
      DAOImg.insert(images)
    }
    Future.sequence(resultF)
  }

}
