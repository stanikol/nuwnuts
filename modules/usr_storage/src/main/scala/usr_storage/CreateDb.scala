package usr_storage

import java.nio.file.{ Files, Path, Paths }
import java.sql.{ DriverManager, ResultSet }
import java.util

import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.typesafe.config.{ Config, ConfigFactory }
import io.getquill._
import model.{ Img, UserDb }

import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import scala.io.Source
import collection.JavaConversions._
import scala.util.Try

object CreateDb {

  def main(args: Array[String]): Unit = {
    implicit val executionContext = concurrent.ExecutionContext.global
    val config = ConfigFactory.load()
    createDb(config)
  }

  private def createDb(config: Config)(implicit executionContext: ExecutionContext) = {
    classOf[org.postgresql.Driver]
    val con_str = "jdbc:" + config.getString("usr_storage.url")
    val conn = DriverManager.getConnection(con_str)
    println(s"Connected to $con_str")
    try {
      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      for { sqlFilename <- Seq("/sql/create-user_db.sql", "/sql/create-db-img.sql") } {
        val sql = Source.fromInputStream(this.getClass.getResourceAsStream(sqlFilename)).mkString
        val rs = stm.execute(sql)
        println(s"`$sqlFilename` was successfully executed on database `$con_str`.")
      }
    } finally {
      conn.close()
    }
  }

}

