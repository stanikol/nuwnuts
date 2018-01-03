package daos.models

case class ImgMeta(
  filename: String,
  alt: Option[String],
  album: Option[String],
  title: Option[String],
  description: Option[String]
)
