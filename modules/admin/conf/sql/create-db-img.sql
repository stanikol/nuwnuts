drop table if exists img;

create table if not exists img(
    id          serial primary key,
    filename    varchar not null unique,
    bytes       bytea not null
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