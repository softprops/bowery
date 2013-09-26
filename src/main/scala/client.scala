package bowery

import dispatch._
import com.ning.http.client.Response

case class Client(conf: Config = Config, http: Http = Http) {
  private def base = url(conf.endpoint)
  import org.json4s._
  import org.json4s.native.JsonMethods._

  private def pkgs(js: JValue) =
    Right(for {
      JArray(ary) <- js
      JObject(fs) <- ary
      ("name", JString(name)) <- fs
      ("url", JString(url)) <- fs
    } yield (name, url))

  private def pkg(js: JValue) =
    Right(for {
      JObject(fs) <- js
      ("name", JString(name)) <- fs
      ("url", JString(url)) <- fs
    } yield (name, url))

  def apply(name: String) =
    for {
      prom <- http(base / name OK as.json4s.Json).either.right
      p    <- Http.promise(pkg(prom))
    } yield p

  def search(name: String) =
    for {
      prom <- http(base / "search" / name OK as.json4s.Json).either.right
      ps   <- Promise(pkgs(prom))
    } yield ps
}
