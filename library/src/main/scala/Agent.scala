package com.deleris.tetrix

class Agent {
    import Stage._

    private[this] val minUtility = -1000.0

    def utility(state: GameState): Double = state.status match {
        case GameOver => minUtility
        case _ => reward(state) - penalty(state) / 10.0
    }

    def reward(s: GameState): Double = s.lineCount.toDouble

    def penalty(s: GameState): Double = {
        val heights = s.unload(s.currentPiece).blocks map {
            _.pos} groupBy {_._1} map { case (k, v) =>
                v.map({_._2 + 1}).max }
        heights map { x => x * x } sum
    }

    def bestMove(s0: GameState): StageMessage = {
        var retval: StageMessage = MoveLeft
        var current: Double = minUtility
        possibleMoves foreach { move =>
            val ms =
                if (move == Drop) move :: Nil
                else move :: Drop :: Nil
            val u = utility(Function.chain(ms map {toTrans})(s0))
            if (u > current) {
                current = u
                retval = move
            }
        }
        retval
    }
        
    private[this] val possibleMoves: Seq[StageMessage] =
        Seq(MoveLeft, MoveRight, RotateCW, Tick, Drop)
    
    private[this] def toTrans(message: StageMessage): GameState => GameState =
        message match {
            case MoveLeft => moveLeft
            case MoveRight => moveRight 
            case RotateCW => rotateCW 
            case Tick => tick 
            case Drop => drop 
        }
}