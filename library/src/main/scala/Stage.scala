package com.deleris.tetrix

class Stage(size: (Int, Int)) {
    private[this] def dropOffPos = (size._1 / 2.0, size._2 - 3.0)
    private[this] var currentPiece = Piece(dropOffPos, TKind)
    private[this] var blocks = Block((0, 0), TKind) +: currentPiece.current
    def view: GameView = GameView(blocks, size, currentPiece.current)

    def moveLeft() = moveBy(-1.0, 0.0)
    def moveRight() = moveBy(1.0, 0.0)
    private[this] def moveBy(delta: (Double, Double)): this.type = {
        validate(
            currentPiece.moveBy(delta),
            unload(currentPiece, blocks)) map { case (moved, unloaded) =>
                blocks = load(moved, unloaded)
                currentPiece = moved
        }        
        this
    }
    private[this] def validate(p: Piece, bs: Seq[Block]): Option[(Piece, Seq[Block])] =
        if (p.current map {_.pos} forall inBounds) Some(p, bs)
        else None
    private[this] def inBounds(pos: (Int, Int)): Boolean =
        (pos._1 >= 0) && (pos._1 < size._1) && (pos._2 >= 0) && (pos._2 < size._2)
    private[this] def unload(p: Piece, bs: Seq[Block]): Seq[Block] = {
        val currentPoss = p.current map {_.pos}
        bs filterNot { currentPoss contains _.pos }
    }
    private[this] def load(p: Piece, bs: Seq[Block]): Seq[Block] =
        bs ++ p.current
}