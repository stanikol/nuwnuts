drop table if exists img;

create table if not exists img(
    id          serial unique,
    filename    varchar not null unique,
    alt         varchar default null,
    album_name  varchar default null,
    description varchar default null,
    image       bytea not null
    ---------------------------
--    constraint unique_filename_album_name unique (filename, album_name)
);

--case class Img(
--  id: Option[Long],
--  filename: String,
--  alt: Option[String],
--  albumName: Option[String],
--  description: Option[String],
--  image: Array[Byte]
--)