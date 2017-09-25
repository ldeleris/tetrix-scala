package com.deleris.tetrix

class Agent {
    def utility(state: GameState): Double = state.status match {
        case GameOver => -1000.0
        case _ => state.lineCount.toDouble
    }

    def bestMove(state: GameState): StageMessage = 
        View
}