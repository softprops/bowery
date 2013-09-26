package bowery

import org.json4s._
import org.json4s.native.JsonMethods._

object Json {
  def apply(in: String) =
    try { Right(parse(in)) }
    catch { case e => Left(e) }
}
