package com.deleris.tetrix

case class GameState(blocks: Seq[Block], gridSize: (Int, Int),
    currentPiece: Piece, nextPiece: Piece, kinds: Seq[PieceKind],
    status: GameStatus = ActiveStatus,
    lineCounts: Seq[Int] = Seq(0, 0, 0, 0, 0),
    lastDeleted: Int = 0,
    pendingAttacks: Int = 0 ) {

    def lineCount: Int =
        lineCounts.zipWithIndex map { case (n, i) => n * i } sum
        
    def view: GameView = GameView(blocks, gridSize,
        currentPiece.current, (4, 4), nextPiece.current,
        status, lineCount, pendingAttacks)
    
    def unload(p: Piece): GameState = {
        val currentPoss = p.current map {_.pos}
        this.copy(blocks = blocks filterNot { currentPoss contains _.pos })
    }

    def load(p:Piece): GameState =
        this.copy(blocks = blocks ++ p.current)
}