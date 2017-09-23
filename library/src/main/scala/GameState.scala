package com.deleris.tetrix

case class GameState(blocks: Seq[Block], gridSize: (Int, Int),
    currentPiece: Piece, nextPiece: Piece, kinds: Seq[PieceKind]) {
    def view: GameView = GameView(blocks, gridSize,
        currentPiece.current, nextPiece.current)
}