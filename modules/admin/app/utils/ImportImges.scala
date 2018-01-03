package utils

import java.nio.file.{ Files, Path, Paths }

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.typesafe.config.{ Config, ConfigFactory }
import daos.DAOImg
import daos.DAOImg.{ insert, log }
import daos.models.Img
import org.slf4j.LoggerFactory

import collection.JavaConverters._
import scala.concurrent.{ Await, ExecutionContext }
import scala.concurrent.duration.Duration

object ImportImages {

  val log = LoggerFactory.getLogger("img")

  def main(args: Array[String]): Unit = {
    import concurrent.ExecutionContext.Implicits.global
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()

    val config = ConfigFactory.load()
    println(s"Loading images from config: ${config.getConfig("img")} ...")
    log.info(s"Loading images from config: ${config.getConfig("img")} ...")
    val resultF = importFiles(config)
    resultF.onComplete(_ => system.terminate())
    val result = Await.result(resultF, Duration.Inf)
    println(result)
  }

  def importFiles(config: Config)(implicit
    executionContext: ExecutionContext,
    system: ActorSystem,
    mat: ActorMaterializer) = {
    Source(readImagesForImport(config)).mapAsync(1)(insert(_)).runFold(0)(_ + _)
  }

  private def readImagesForImport(config: Config): List[Img] = {
    val supportedExtensions: List[String] = config.getStringList("img.extensions").asScala.toList
    val importFrom = config.getStringList("img.import-dirs").asScala.toList
    (
      for { importDir <- importFrom } yield {
        val foundImagePaths: Set[Path] = Files.walk(Paths.get(importDir)).iterator().asScala.toSet
          .filter(f => f.toFile.isFile && supportedExtensions.exists(f.toString.endsWith(_)))
        foundImagePaths.map { imgPath =>
          val filename = imgPath.getFileName.toString
          Img.fromPath(filename, imgPath).fold(
            { error =>
              log.error(s"Error while reading `$filename`: ${error.getMessage} !")
              None
            },
            Some(_)
          ): Option[Img]
        }.filter(_.isDefined).map(_.get).toList: List[Img]
      }
    ).flatten.groupBy(_.filename).mapValues(_.head).values.toList
  }

}
