package com.deleris.tetrix
import akka.actor._

class AgentActor(stageActor: ActorRef) extends Actor {
    private[this] val agent = new Agent 

    def receive = {
        case BestMoves(s, config) =>
          agent.bestMoves(s, config.maxThinkTime) match {
            case Seq(Tick) => // do nothing
            case Seq(Drop) => config.onDrop map { stageActor ! _ }
            case ms        =>
              ms foreach { _ match {
                case Tick | Drop => // do nothing
                case m           =>
                  stageActor ! m
                  Thread.sleep(config.minActionTime)
              }}
          }
          sender ! ()
    }
}

