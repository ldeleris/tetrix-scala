package com.deleris.tetrix

object Stage {
    import scala.annotation.tailrec

    def newState(blocks: Seq[Block], gridSize: (Int, Int),
        kinds: Seq[PieceKind]) : GameState = {
 
        val dummy = Piece((0, 0), TKind)
        val withNext = spawn(GameState(Nil, gridSize, dummy, dummy, kinds)).
            copy(blocks = blocks)
        spawn(withNext)
    }

    val moveLeft = transit { _.moveBy(-1.0, 0.0) }
    val moveRight = transit { _.moveBy(1.0, 0.0) }
    val rotateCW = transit { _.rotateBy(-math.Pi / 2.0) }

    val tick = transit (_.moveBy(0.0, -1.0),
        Function.chain(clearFullRow :: spawn :: Nil) )

    val drop: GameState => GameState = (s0: GameState) =>
        Function.chain((Nil padTo (s0.gridSize._2, transit {_.moveBy(0.0, -1.0)})) ++
            List(tick))(s0)

    def randomStream(random: scala.util.Random): Stream[PieceKind] =
        PieceKind(random.nextInt % 7) #:: randomStream(random)

    private[this] lazy val clearFullRow: GameState => GameState =
        (s0: GameState) => {
            def isFullRow(i: Int, s: GameState): Boolean =
                (s.blocks filter {_.pos._2 == i} size) == s.gridSize._1
            @tailrec def tryNow(i: Int, s: GameState): GameState =
                if (i < 0) s 
                else if (isFullRow(i, s))
                    tryNow(i - 1, s.copy(blocks = (s.blocks filter {_.pos._2 < i}) ++
                        (s.blocks filter {_.pos._2 > i} map { b =>
                            b.copy(pos = (b.pos._1, b.pos._2 - 1)) }),
                        lastDeleted = s.lastDeleted + 1))
                else tryNow(i - 1, s)
            val s1 = tryNow(s0.gridSize._2 - 1, s0)
            if (s1.lastDeleted == 0) s1
            else s1.copy(lineCounts = s1.lineCounts updated
                (s1.lastDeleted, s1.lineCounts(s1.lastDeleted) + 1))
        }

    private[this] lazy val spawn: GameState => GameState = 
        (s: GameState) => {
            def dropOffPos = (s.gridSize._1 / 2.0, s.gridSize._2 - 3.0)
            val s1 = s.copy(blocks = s.blocks,
                currentPiece = s.nextPiece.copy(pos = dropOffPos),
                nextPiece = Piece((2, 1), s.kinds.head),
                kinds = s.kinds.tail)
            validate(s1) map { case x =>
                x.load(x.currentPiece)
            } getOrElse {
                s1.load(s1.currentPiece).copy(status = GameOver)
            }
    }

    private[this] def transit(trans: Piece => Piece,
        onFail: GameState => GameState = identity): GameState => GameState = 
        (s: GameState) => s.status match {
            case ActiveStatus =>
                validate(s.unload(s.currentPiece).copy(
                    currentPiece = trans(s.currentPiece))) map { case x =>
                        x.load(x.currentPiece)
                } getOrElse {onFail(s)} 
            case _ => s 
        }

    private[this] def validate(s: GameState): Option[GameState] = {
        val size = s.gridSize
        def inBounds(pos: (Int, Int)): Boolean =
            (pos._1 >= 0) && (pos._1 < size._1) && (pos._2 >= 0) && (pos._2 < size._2)
        val currentPoss = s.currentPiece.current map {_.pos}
        if ((currentPoss forall inBounds) &&
            (s.blocks map {_.pos} intersect currentPoss).isEmpty) Some(s)
        else None
    }
    def toTrans(message: StageMessage): GameState => GameState =
        message match {
            case MoveLeft  => moveLeft
            case MoveRight => moveRight
            case RotateCW  => rotateCW
            case Tick      => tick
            case Drop      => drop
        }  
}