name := "kafkaProducer"

version := "0.1"

scalaVersion := "2.11.8"

/************************************************
  * Kafka Dependencies ver 2.0
  ************************************************/
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.0.0"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.0.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.6.3" // Producer


/************************************************
  * Twitter Dependencies ver 4.0
  ************************************************/
//twitterStreaming
//libraryDependencies += "com.typesafe" % "config" % "1.3.1"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.4",
  "org.twitter4j" % "twitter4j-stream" % "4.0.4")


/************************************************
  * Spark & Hadoop Dependencies
  ************************************************/
lazy val hadoopVersion = "2.7.5"
lazy val sparkVersion = "2.3.0"

libraryDependencies +=  "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion
libraryDependencies +=  "org.apache.hadoop" % "hadoop-aws" % hadoopVersion
libraryDependencies +=  "org.apache.hadoop" % "hadoop-common" % hadoopVersion

libraryDependencies +=  "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies +=  "org.apache.spark" %% "spark-mllib" % sparkVersion
libraryDependencies +=  "org.apache.spark" %% "spark-streaming" % sparkVersion
libraryDependencies +=  "org.apache.spark" %% "spark-hive" % sparkVersion
//libraryDependencies += "org.apache.spark" %% "spark-graphx" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.0.0"


/************************************************
  * Other Dependencies
  ************************************************/