import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "PlaySandbox"
  val appVersion      = "0.2.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    "org.mongojack" %% "play-mongojack" % "2.0.0-RC2",
    "org.mongojack" % "mongojack" % "2.0.0-RC5"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
