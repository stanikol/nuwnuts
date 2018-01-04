package utils

/**
 * Created by snc on 4/27/17.
 */

import java.io._
import java.net.{ HttpURLConnection, URL }

import org.jsoup.Jsoup
import org.jsoup.helper.Validate
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import collection.JavaConversions._
import sys.process._
import scala.language.postfixOps

object GrabOreh extends App {
  // Сначала грабим статьи
  val url: String = "http://oreh.od.ua/article"
  val doc: Document = Jsoup.connect(url).get();
  val articlesLinks = doc.select(".article > .art_title > a")
  val articlesIndex = articlesLinks.map { element: Element =>
    val articleID = element.attr("href").split("/").last.toInt
    articleID -> s"$articleID\t${element.attr("href")}\t${element.text()}"
  }.sortBy(_._1).map(_._2).mkString("\n")
  // сохраняем перечень статей
  new File("grab2/articles") mkdirs ()
  new FileWriter("grab2/articles/article-index.tsv") {
    write(articlesIndex)
    close()
  }
  println(s"Articles.lenght = ${articlesLinks.length}")
  // грабим каждую статью
  articlesLinks.map { element: Element =>
    val url = s"http://oreh.od.ua${element.attr("href")}"
    val articleID = element.attr("href").split("/").last.toInt
    println(s"Opening article #$articleID at $url ...")
    val doc = Jsoup.connect(url).get()
    // сохраняем htm статьи
    val articleFolder = s"grab2/articles/$articleID"
    new File(articleFolder) mkdirs ()
    val article = doc.select("#item_full")
    new FileWriter(new File(articleFolder, s"$articleID.html")) {
      write(article.html())
      close()
    }
    // сохраняем картинки
    val images = doc.select("#item_full img")
    images.map { element: Element =>
      val imageUrl = new URL(s"http://oreh.od.ua${element.attr("src")}")
      val filename = imageUrl.getFile.split("/").last
      println(s" Downloading and saving image $imageUrl to ${filename} ...")
      imageUrl #> new File(articleFolder, filename) !!
    }
  }
  // Грабим орехи
  val doc2: Document = Jsoup.connect("http://oreh.od.ua/items").get();
  val nutsLinksAndTitles = doc2.select("div.category a.title").map { a: Element =>
    a.attr("href") -> a.attr("title")
  }
  val tsv = nutsLinksAndTitles.map {
    case (href: String, title: String) =>
      println(s"Processing $href ...")
      val doc = Jsoup.connect(s"http://oreh.od.ua$href").get()
      val id = href.split("/").last
      val title = doc.select("#item_title").text()
      val category = doc.select("#cat").text()
      val html: String = doc.select("#item_full").html()
      // save html
      new File(s"grab2/blog/$id") mkdirs ()
      new PrintWriter(s"grab2/blog/$id/$id.html") {
        write(html)
        close()
      }
      //
      val imagePath = doc.select("img.someitem").first.attr("src")
      val url = new URL(s"http://oreh.od.ua$imagePath")
      val imageFilename = imagePath.split("/").last
      url #> new File(s"grab2/blog/$id/$imageFilename") !!
      //
      val tsvLine = s"$id\t$title\t$category\t$imageFilename\n"
      tsvLine
  }

  new FileWriter("grab2/blog/blog-index.tsv") {
    write(tsv.mkString(""))
    close()
  }
}
