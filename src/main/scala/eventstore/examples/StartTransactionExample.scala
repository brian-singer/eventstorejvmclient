package eventstore.examples

import akka.actor.{ Props, ActorSystem }
import eventstore.TransactionActor._
import eventstore.tcp.ConnectionActor
import eventstore.{ EventData, TransactionActor, EventStream, TransactionStart }

object StartTransactionExample extends App {
  val system = ActorSystem()
  val connection = system.actorOf(ConnectionActor.props(), "connection")

  val kickoff = Start(TransactionStart(EventStream("my-stream")))
  val transaction = system.actorOf(TransactionActor.props(connection, kickoff), "transaction")
  implicit val transactionResult = system.actorOf(Props[TransactionResult], "result")

  transaction ! GetTransactionId // replies with `TransactionId(transactionId)`
  transaction ! Write(EventData("transaction-event")) // replies with `WriteCompleted`
  transaction ! Write(EventData("transaction-event")) // replies with `WriteCompleted`
  transaction ! Write(EventData("transaction-event")) // replies with `WriteCompleted`
  transaction ! Commit // replies with `CommitCompleted`
}