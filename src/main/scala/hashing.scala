package bowery

import java.security.MessageDigest

trait Hashing {
  def hash(values: Any*): String = {
    val dig = MessageDigest.getInstance("MD5")
    dig.update(values.mkString("%").getBytes())
    val bytes = dig.digest()
    val b = new StringBuilder()
    for (byte <- bytes) b.append("%s0x", byte & 0xff)
    b.toString
  }
}
