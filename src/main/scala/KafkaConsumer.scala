import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{ConsumerStrategies,_}

object kafkaConsumer{

  def main(args: Array[String]) {

    val conf=new SparkConf().setAppName("Twitter").setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(2))
    //val ssc  = new StreamingContext("local[*]","streamtwitter",Seconds(1))
    val topics = List("twitter").toSet
    val kafkaParams = Map(
      "bootstrap.servers"-> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark-streaming-something",
      "auto.offset.reset" -> "earliest")

    val line = KafkaUtils.createDirectStream[String,String](
      ssc,
      PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams))


    val temp = line.map(record=>record.value().toString.toLowerCase)
    val _line = temp.flatMap(_.split(","))

    _line.print

    ssc.start()
    ssc.awaitTermination

  } // End of main

} // End of ob


