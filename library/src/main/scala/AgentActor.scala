package com.deleris.tetrix
import akka.actor._

class AgentActor(stageActor: ActorRef) extends Actor {
    private[this] val agent = new Agent 

    def receive = {
        case BestMove(s: GameState) =>
            val message = agent.bestMove(s)
            if (message != Drop) stageActor ! message
    }
}

