package com.deleris.tetrix

class AbstractUI(config: Config) {
  //import Stage._
  //import java.{util => ju}
  import akka.actor._
  import akka.pattern.ask
  import scala.concurrent.duration._
  import akka.util.Timeout
  import scala.concurrent._
  implicit val timeout = Timeout(100 millisecond)
  import ExecutionContext.Implicits.global

  private[this] val initialState = Stage.newState(Nil,
    (10, 23), randomStream(new scala.util.Random))
  private[this] val system = ActorSystem("TetrixSystem")

  private[this] val stateActor1 = system.actorOf(Props(new StateActor(
    initialState)), name = "stateActor1")
  private[this] val playerActor1 = system.actorOf(Props(new StageActor(
    stateActor1)), name = "playerActor1")

  private[this] val stateActor2 = system.actorOf(Props(new StateActor(
    initialState)), name = "stateActor2")
  private[this] val playerActor2 = system.actorOf(Props(new StageActor(
    stateActor2)), name = "playerActor2")

  private[this] val agentActor = system.actorOf(Props(new AgentActor(
    playerActor2)), name = "agentActor")

  private[this] val masterActor = system.actorOf(Props(new GameMasterActor(
    stateActor1, stateActor2, agentActor, config: Config)), name = "masterActor")

  private[this] val tickTimer1 = system.scheduler.schedule(
    0 millisecond, 701 millisecond, playerActor1, Tick)
  private[this] val tickTimer2 = system.scheduler.schedule(
    0 millisecond, 701 millisecond, playerActor2, Tick)

  private[this] def randomStream(random: scala.util.Random): Stream[PieceKind] =
    PieceKind(random.nextInt % 7) #:: randomStream(random)

  masterActor ! Start

  def left() { playerActor1 ! MoveLeft }
  def right() { playerActor1 ! MoveRight }
  def up() { playerActor1 ! RotateCW }
  def down() { playerActor1 ! Tick }
  def space() { playerActor1 ! Drop }

  def view: (GameView, GameView) = 
    (Await.result((stateActor1 ? GetView).mapTo[GameView], timeout.duration),
    Await.result((stateActor2 ? GetView).mapTo[GameView], timeout.duration))

  def restart() { 
    val initialState = Stage.newState(Nil,
      (10, 23), randomStream(new scala.util.Random))
    
    masterActor ! ReStart(initialState)
  }
}