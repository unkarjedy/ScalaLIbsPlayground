package _akka._streams.quickstart

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object Example4 extends App {
  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  // Explicitly creating and wiring up a Source, Sink and Flow
  Source(1 to 6).via(Flow[Int].map(_ * 2))
    .to(Sink.foreach(println(_)))

  // Starting from a Source
  val source = Source(1 to 6).map(_ * 2)
  source.to(Sink.foreach(println(_)))

  // Starting from a Sink
  val sink: Sink[Int, NotUsed] = Flow[Int].map(_ * 2).to(Sink.foreach(println(_)))
  Source(1 to 6).to(sink)

  // Broadcast to a sink inline
  val otherSink: Sink[Int, NotUsed] =
    Flow[Int]
      .alsoTo(Sink.foreach(println(_)))
      .alsoTo(Sink.foreach(x => println("ALOHA" + x)))
      .to(Sink.ignore)
  Source(1 to 6).to(otherSink).run()
}
