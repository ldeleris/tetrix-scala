package com.deleris.tetrix

case class GameState(blocks: Seq[Block], gridSize: (Int, Int),
    status: GameStatus, lineCount: Int,
    currentPiece: Piece, nextPiece: Piece, kinds: Seq[PieceKind]) {
    def view: GameView = GameView(blocks, gridSize,
        currentPiece.current, (4, 4), nextPiece.current,
        status, lineCount)
    
    def unload(p: Piece): GameState = {
        val currentPoss = p.current map {_.pos}
        this.copy(blocks = blocks filterNot { currentPoss contains _.pos })
    }

    def load(p:Piece): GameState =
        this.copy(blocks = blocks ++ p.current)
}