package com.deleris.tetrix

case class GameState(blocks: Seq[Block], gridSize: (Int, Int), currentPiece:Piece) {
    def view: GameView = GameView(blocks, gridSize, currentPiece.current)
}