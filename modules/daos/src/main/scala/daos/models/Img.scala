package daos.models

import java.nio.file.{ Files, Path }

import scala.util.Try

case class Img(filename: String, bytes: Array[Byte])

object Img {
  def fromPath(filename: String, imagePath: Path): Try[Img] = Try {
    val bytes = Files.readAllBytes(imagePath)
    new Img(filename, bytes)
  }
}
