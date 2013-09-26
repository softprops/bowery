package bowery

import java.io.File
import org.eclipse.jgit.api._
import scala.collection.JavaConverters._

object Git {
  // git checkout {tag} -f
  def checkout(git: Git, tag: String) =
    Option(git.checkout()
               .setName(tag)
               .setForce(true)
               .call).map(_.getName)

  // git clean -f -d
  def clean(git: Git) =
    git.clean().setCleanDirectories(true).call

  // git log ...
  def commits(git: Git, n: Int) =
    git.log().setMaxCount(n).call.asScala.map(_.getName)

  // list tags
  def tag(git: Git) =
    git.tagList().call().asScala.map(_.getName)

  // git reset --hard
  def reset(git: Git, to: String) =
    git.reset().setRef(to).call

  // git fetch --prune
  def fetch(git: Git) =
    git.fetch()

  // git clone {uri} {to}
  def clone(url: String, to: String) =
    new CloneCommand().setURI(url).setDirectory(new File(to)).call()
}
