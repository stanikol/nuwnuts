package utils

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import collection.JavaConverters._
import com.typesafe.config.ConfigFactory
import daos.models.Blog
import org.slf4j.LoggerFactory

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object ImportBlog {
  import concurrent.ExecutionContext.Implicits.global
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val logger = LoggerFactory.getLogger("blog")
  val config = ConfigFactory.parseFile(Paths.get("/Users/snc/scala/newnuts/conf/blog.conf").toFile)

  def main(args: Array[String]): Unit = {
    val importDir = config.getString("blog.import-dir")
    logger.info("Going to import blog posts form `{}` ...", importDir)

    val blogs = Files.readAllLines(Paths.get(importDir, "article-index.tsv"))
      .asScala.toList
      .map { line =>
        val rec = line.split("\t")
        val blogId: Long = rec(0).toLong
        val title: String = rec.last
        val html = Files.readAllLines(Paths.get(importDir, blogId.toString, s"$blogId.html"))
          .asScala.mkString("\n").replace("/assets/images/", "/img/")
        Blog.empty.copy(blogId = blogId, title = title, html = html)
      }

    val f = Source(blogs)
      .mapAsync(2){blog=>
        import daos.psql._
        val q = quote(query[Blog].insert(lift(blog)))
        daos.psql.run(q).map(_=> blog)
      }.runWith(Sink.seq)



    println(Await.result(f, Duration.Inf))
    system.terminate()
    //    val importPath = Paths.get(importDir)
    //    val articlesPaths = Files.list(importPath).filter { p =>
    //      Files.isDirectory(p) && p.getFileName.toString.matches("\\d+")
    //    }.iterator().asScala.toList
    //    val htmlPaths = articlesPaths.map { ap =>
    //      val filename = ap.getFileName
    //      Paths.get(ap.toString, filename.toString + ".html")
    //    }
    //    htmlPaths.foreach { p =>
    //      val html = new String(Files.readAllBytes(p), StandardCharsets.UTF_8)
    //        .replace("/assets/images/", "/img/")
    //      val blodId = p.getParent.getFileName.toString.toLong
    //
    //      println(html)
    //    }

  }

}
