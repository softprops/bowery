package bowery

import java.io.File
 
trait Config {
  def cache: String
  def dir: String
  def json: String
  def endpoint: String
}

object Config extends Config {
  def bowdir = (System.getProperty("user.home") :: ".bowery" :: Nil)
                .mkString(File.separator)
  def cache = (bowdir :: "cache" :: Nil)
                .mkString(File.separator)
  def dir = "components"
  def json = "component.json"
  def endpoint = "https://bower.herokuapp.com"
}
