package view
import io.getquill.ast.NumericOperator.>

import scala.io.Source
import scalatags.Text.all._
import scalatags.Text.tags2.nav

object NavBar {

  def apply() = raw(Source.fromInputStream(this.getClass.getResourceAsStream("/img/html/navbar.html")).mkString)

}
