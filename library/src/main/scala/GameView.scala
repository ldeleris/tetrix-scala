package com.deleris.tetrix

case class GameView(blocks: Seq[Block], gridSize: (Int, Int),
    current: Seq[Block], miniGridSize: (Int, Int), next: Seq[Block],
    status: GameStatus, lineCount: Int)