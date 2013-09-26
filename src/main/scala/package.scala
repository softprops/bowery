package bowery

import java.io._
import semverfi._

case class Package(
  maybename: Option[String], endpoint: String)
  extends Hashing {

  val dependencies = Map.empty[String, Package]

  val resolver = Resolver(url)

  // guess name
  val name = maybename.getOrElse(endpoint.possibleName)

  // uniq id
  val id = hash(name, endpoint.url, endpoint.tag)
  
  // ash
  val localDir = new File(".", Seq(Config.dir, name)
                                .mkString(File.separator))

  // store git specifics for caching 
  val (resourceId, gitPath): (Option[String], Option[String]) =
    endpoint match {
      case GitRepo(url, tag) =>
        val resourceId = hash(name, url)
        (Some(resourceId),
         Some(Seq(Config.cache, name, resourceId)
                .mkString(File.pathSeparator)))
      case _ =>
        (None, None)
    }

  def resolve: Either[String, Package] =
    endpoint match {

      case Named(name, tag) =>
        // resolve the name and url from server
        Client()(name)().fold(t => Left(t.getMessage), _.headOption.map {
          case (name, url) =>
            // resolve as git repo
            Package(Some(name),
                    tag.map(url + "#" + _).getOrElse(url))
              .resolve
        }.getOrElse(Left("Package named %s not found" format name)))

      case GitRepo(url, tag) =>
        // make cache dir
        val cache = new File(Config.cache)
        cache.mkdirs()

        // make the git path if nessessary
        val gitDir = new File(gitPath.get)
        if (gitDir.exists) Right(this) // its already cached
        else {
          gitDir.mkdirs()

          val repo = Git.clone(url, gitPath.get)
          if (!gitDir.exists) Left(
            "unable to fetch %s. try recache" format name)
          else {
            Git.fetch(repo)
            Git.reset(repo, Option(url).map(_ =>"origin/HEAD")
                                       .getOrElse("HEAD"))

            val versions = Git.tag(repo) ++ Git.commits(repo, 1)
            if (versions.isEmpty) Right(this)
            else tag.map { t =>
              Version(t) match {
                case Invalid(_) => Left("Invalid tag %s" format t)
                case tv =>
                  versions.filter(_ => true/*.satisfies(tv)*/) match {
                    case e if (e.isEmpty) =>
                      Left("Could not find tag statisfying %s" format tag)
                    case vx =>
                      Right(vx(0))
                  }
              }
            }.getOrElse(Right(versions(0))) match {
              case Right(ver) =>
                Version(ver) match {
                  case Invalid(_) => ()
                  case _ => ()
                }
                Git.checkout(repo, ver)
                Git.clean(repo)
                Right(this)
            }
          }
        }
    }
}
