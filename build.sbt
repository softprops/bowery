// http://harrah.github.com/browse/samples/compiler/scala/tools/nsc/io/ZipArchive.scala.html

organization := "me.lessis"

name := "bowery"

version := "0.1.0-SNAPSHOT"

crossScalaVersions := Seq("2.9.3", "2.10.0", "2.10.1")

libraryDependencies += "net.databinder.dispatch" %% "json4s-native" % "0.9.4"

libraryDependencies += "me.lessis" %% "semverfi" % "0.1.1"

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "2.2.0.201212191850-r"
