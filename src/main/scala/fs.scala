package bowery

import java.io.{ File, FileInputStream, FileOutputStream }

/*object TmpFile {
  def apply[T](path: String, f: File => T) = {
    val tmp = File.createTempFile()
    tmp.deleteOnExit()
  }
}*/

object Copy {
  def apply(from: File, to: File) = {
    val fin  = new FileInputStream(from).getChannel
    val fout = new FileInputStream(to).getChannel
    fin.transferTo(0, fin.size, fout)
    fout.close
    fin.close
  }
}
