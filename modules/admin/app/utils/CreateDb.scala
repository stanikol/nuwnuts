package utils

import java.sql.{ DriverManager, ResultSet }

import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.ExecutionContext
import scala.io.Source

object CreateDb {

  def main(args: Array[String]): Unit = {
    implicit val executionContext = concurrent.ExecutionContext.global
    val config = ConfigFactory.load()
    createDb(config)
  }

  private def createDb(config: Config)(implicit executionContext: ExecutionContext) = {
    classOf[org.postgresql.Driver]
    val con_str = "jdbc:" + config.getString("daos.url")
    val conn = DriverManager.getConnection(con_str)
    println(s"Connected to $con_str")
    try {
      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      for { sqlFilename <- Seq("/sql/create-db-user.sql", "/sql/create-db-img.sql", "/sql/create-db-blog.sql") } {
        val sql = Source.fromInputStream(this.getClass.getResourceAsStream(sqlFilename)).mkString
        val rs = stm.execute(sql)
        println(s"`$sqlFilename` was successfully executed on database `$con_str`.")
      }
    } finally {
      conn.close()
    }
  }

}

