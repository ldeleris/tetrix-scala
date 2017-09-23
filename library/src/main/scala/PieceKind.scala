package com.deleris.tetrix

sealed trait PieceKind
case object IKind extends PieceKind
case object JKind extends PieceKind
case object LKind extends PieceKind
case object OKind extends PieceKind
case object SKind extends PieceKind
case object TKind extends PieceKind
case object ZKind extends PieceKind

case object PieceKind {
    def apply(x: Int): PieceKind = x match {
        case 0 => IKind
        case 1 => JKind
        case 2 => LKind
        case 3 => OKind
        case 4 => SKind
        case 5 => TKind
        case _ => ZKind
    }
}

case class Block(pos: (Int, Int), kind: PieceKind)
case class GameView(blocks: Seq[Block], gridSize: (Int, Int),
    current: Seq[Block], next: Seq[Block])