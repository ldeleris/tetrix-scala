package com.deleris.tetrix

class AbstractUI {
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
  private[this] val stateActor = system.actorOf(Props(new StateActor(
    initialState)), name = "stateActor")
  private[this] val playerActor = system.actorOf(Props(new StageActor(
    stateActor)), name = "playerActor")
  private[this] val agentActor = system.actorOf(Props(new AgentActor(
    playerActor)), name = "agentActor")
  private[this] val masterActor = system.actorOf(Props(new GameMasterActor(
    stateActor, agentActor)), name = "masterActor")
  private[this] val tickTimer = system.scheduler.schedule(
    0 millisecond, 700 millisecond, playerActor, Tick)
  private[this] val masterTickTimer = system.scheduler.schedule(
    0 millisecond, 700 millisecond, masterActor, Tick)
  private[this] def randomStream(random: scala.util.Random): Stream[PieceKind] =
    PieceKind(random.nextInt % 7) #:: randomStream(random)

  def left() { playerActor ! MoveLeft }
  def right() { playerActor ! MoveRight }
  def up() { playerActor ! RotateCW }
  def down() { playerActor ! Tick }
  def space() { playerActor ! Drop }
  def view: GameView = 
    Await.result((stateActor ? GetView).mapTo[GameView], timeout.duration)
}