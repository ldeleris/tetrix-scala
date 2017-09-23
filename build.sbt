lazy val buildSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  organization := "com.deleris",
  homepage := Some(url("http://deleris.com")),
  licenses := Seq("MIT License" -> url("http://opensource.org/licenses/mit-license.php/")),
  scalaVersion := "2.10.2",
  scalacOptions := Seq("-deprecation", "-unchecked"),
  initialCommands in console := """import com.deleris.tetrix._
                                  |import Stage._""".stripMargin,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("public"),
    Resolver.typesafeRepo("releases")
  )
)

lazy val specs2version = "2.2.2"
lazy val libDeps = Def.setting { Seq(
  "org.specs2" %% "specs2" % specs2version % "test"
)}
lazy val swingDependencies = Def.setting {
  "org.scala-lang" % "scala-swing" % scalaVersion.value
}

lazy val root = (project in file(".")).
  settings(buildSettings: _*).
  settings(name := "tetrix.scala")

lazy val library = (project in file("library")).
  settings(buildSettings: _*).
  settings(
    name := "tetrix_library",
    libraryDependencies ++= libDeps.value,
    exportJars := true
  )

lazy val swing = (project in file("swing")).
  settings(buildSettings: _*).
  settings(
    fork in run := true,
    libraryDependencies += swingDependencies.value
  ).
  dependsOn(library)

