package daos.model

import java.nio.file.{ Files, Path }

import scala.util.Try

case class Img(
  id: Option[Long],
  filename: String,
  alt: Option[String],
  albumName: Option[String],
  description: Option[String],
  image: Array[Byte]
)

object Img {
  def fromPath(imgPath: Path): Try[Img] = Try {
    val bytea: Array[Byte] = Files.readAllBytes(imgPath)
    val filename: String = imgPath.getFileName.toString
    new Img(None, filename, None, None, None, bytea)
  }
}
