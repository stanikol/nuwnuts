import java.nio.file.{Files, Paths}
import collection.JavaConverters._

val grab = "/Users/snc/scala/newnuts/grab/articles"
Files.list(Paths.get(grab))
    .filter(p => p.toFile.isDirectory && p.getFileName.toString.matches("\\d+"))
  .iterator().asScala.toList.sorted.mkString("\n")