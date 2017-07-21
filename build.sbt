lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)

name := "akka-http-docker-aws-code-pipeline-beanstalk"

version := "latest"

scalaVersion := "2.11.8"

packageName in Docker := "sample-repository"
dockerExposedPorts := Seq(5000)
dockerUpdateLatest := true
dockerRepository := Some("REPLACE.WITH.YOUR.REPOSITORY.URI")


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"         % "10.0.9",
  "com.typesafe.akka" %% "akka-slf4j"        % "2.4.19",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % "test",
  "org.scalatest"     %% "scalatest"         % "3.0.0" % "test",
  "org.json4s"        %% "json4s-jackson"    % "3.5.2",
  "ch.qos.logback"    % "logback-classic"    % "1.2.3"
)

unmanagedResourceDirectories in Compile += {
  baseDirectory.value / "src/main/resources"
}
