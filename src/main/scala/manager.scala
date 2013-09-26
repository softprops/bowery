package bowery

import java.io.File
import scala.collection.JavaConverters._

class Manager(endpoints: Seq[String]) {

  def resolve = {
    val localDeps = resolveLocal
    val remoteDeps =
      if (endpoints.isEmpty) resolveFromJson
      else resolveFromEndpoints
    prune(localDeps ++ remoteDeps)
  }

  def resolveLocal =
    (Map.empty[String, Package] /: new File("./" + Config.dir)
      .listFiles.asScala) {
      case (deps, dir) =>
        val name = dir.getName
        deps + (name, new Package(name, dir.getAbsolutePath))
      }
}
