package _akka._streams.quickstart

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString

import scala.concurrent.Future

object ReusablePieces extends App {
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  def lineSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(s => ByteString(s + "\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

  val source: Source[Int, NotUsed] = Source(1 to 100)
  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  factorials
    .map(_.toString)
    .runWith(lineSink("factorial2.txt"))
}
