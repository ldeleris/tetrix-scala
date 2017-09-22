package com.deleris.tetrix

sealed trait PieceKind
case object IKind extends PieceKind
case object JKind extends PieceKind
case object LKind extends PieceKind
case object OKind extends PieceKind
case object SKind extends PieceKind
case object TKind extends PieceKind
case object ZKind extends PieceKind

case class Block(pos: (Int, Int), kind: PieceKind)
case class GameView(blocks: Seq[Block], gridSize: (Int, Int), current: Seq[Block])