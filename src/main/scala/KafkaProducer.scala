import java.util.concurrent.LinkedBlockingQueue
import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

object kafkaProducer {

  def main(args: Array[String]): Unit = {

    //variables to store specific data need.
    val queue = new LinkedBlockingQueue[Status](1000)
    val consumerKey = "Emsmhh8jR8wMETqxOxyvBvhqB"
    val consumerSecret = "tfwpgvvj7G1lCgZ529RmAx6sB61LKcpSFSrIx7KY5RfwMMAvUr"
    val accessToken = "196342002-d2AVkG6SCCIBv23tWSWKDx4d8PyspG3LMd8E22Il"
    val accessTokenSecret = "4e2G2UsYEDesfhusQqgotXAEK45X0urLDVdUiSB182T8T"
    val topicName = "twitter"
    val keywords = "Trump"
    val confBuild = new ConfigurationBuilder()

    // assign authentication tokens to configuration
    confBuild
      .setDebugEnabled(true)
      .setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)

    //using the build configurations for twitter stream creation
    val stream = new TwitterStreamFactory(confBuild.build()).getInstance()

    // initalizing listener
    val listener = new StatusListener {

      override def onStatus(status: Status): Unit = {
        queue.offer(status)
      } //end of onStatus override

      override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {
        println("Got a status deletion notice id:"
          + statusDeletionNotice.getStatusId)
      } //end of onDeletionNotice override

      override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {
        println("Got track limitation notice:"
          + numberOfLimitedStatuses)
      } //end of onTrackLimitationNotice override

      override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {
        println("Got scrub_geo event userId:"
          + userId
          + "upToStatusId:"
          + upToStatusId)
      } //end of onScrubGeo override

      override def onStallWarning(warning: StallWarning): Unit = {
        println("Got stall warning:"
          + warning)
      } // end of onStallWarning override

      override def onException(ex: Exception): Unit = {
        ex.printStackTrace()
      } // end of onException override

    } // End of listener

    stream
      .addListener(listener)

    // filter the search by keywords
    val query = new FilterQuery(keywords)

    stream
      .filter(query)

    val properties = new Properties()

    properties.put("metadata.broker.list", "localhost:9092")
    properties.put("bootstrap.servers", "localhost:9092")
    properties.put("retries", "0")
    properties.put("batch.size", "16384")
    properties.put("linger.ms", "1")
    properties.put("buffer.memory", "33554432")
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](properties)

    var count: Int = 0

    while (true) {
      val status = queue.poll()
      if (status == null) { // no new tweets -- so wait
        Thread.sleep(100)
      } else { // there are tweets -- so send them to the topic
        for (hashtagEntity <- status.getHashtagEntities) {
          println("Tweet: " + status + "\nHashtag: " + hashtagEntity.getText)
          producer.send(new ProducerRecord[String, String](
            topicName,
            (count += 1).toString,
            status.getText))
        }
      } // End of else
    } // End of while

    producer.close()
    Thread.sleep(500)
    stream.shutdown()

  }
}