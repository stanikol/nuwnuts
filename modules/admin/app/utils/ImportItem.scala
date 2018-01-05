package utils

import java.nio.charset.StandardCharsets
import java.nio.file.{ Files, Paths }

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Sink, Source }

import collection.JavaConverters._
import com.typesafe.config.ConfigFactory
import daos.models.{ Blog, Item, ItemCategory }
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object ImportItem {
  import concurrent.ExecutionContext.Implicits.global
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import daos.psql._
  val logger = LoggerFactory.getLogger("blog")
  val config = ConfigFactory.parseFile(Paths.get("/Users/snc/scala/newnuts/conf/item.conf").toFile)

  def main(args: Array[String]): Unit = {
    val importDir = config.getString("item.import-dir")
    logger.info("Going to import blog posts form `{}` ...", importDir)

    val tsv = Files.readAllLines(Paths.get(importDir, "nuts-index.tsv"))
      .asScala.toList
    val categories = tsv.map(_.split("\t")(2): String).toSet.zipWithIndex
      .map {
        case (name: String, idx) =>
          val newCategory = ItemCategory(idx, name, None)
          val q = quote(query[ItemCategory].insert(lift(newCategory)))
          Await.result(daos.psql.run(q), Duration.Inf)
          newCategory
      }

    val items = tsv
      .map { line =>
        val rec = line.split("\t")
        val id: Long = rec(0).toLong
        val title: String = rec(1)
        val html = Files.readAllLines(Paths.get(importDir, id.toString, s"$id.html"))
          .asScala.mkString("\n").replace("/assets/images/", "/img/")
        val categoryName = rec(2)
        val categoryId = categories.filter(_.category == categoryName).head.id
        val img = rec(3)
        val item = Item(id, categoryId, Some(title), Some(img), Some(html), None, true, None, None)
        item
      }

    val f = Source(items)
      .mapAsync(2) { item =>
        val q = quote(query[Item].insert(lift(item)))
        daos.psql.run(q).map { blogId =>
          logger.info("Imported blog post with ID={}", blogId)
          1
        }
      }.runFold(0)(_ + _)

    println(Await.result(f, Duration.Inf))
    system.terminate()
  }

}
