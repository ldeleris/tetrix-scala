package com.deleris.tetrix

object Scripter {
    import Stage._

    def main(args: Array[String]) {
        args.toList match {
            case "generate" :: Nil => println(generateScript)
            case "test" :: Nil => testAll
            case _ =>
                println("-> run test")
                println("-> run generate")
        }
    }

    def testAll {
        val scriptDir = new java.io.File("script")
        for (i <- 0 to 1)
            test(new java.io.File(scriptDir, i.toString + ".txt"))
    }

    def test(file: java.io.File) {
        val pieces = io.Source.fromFile(file).seq map { c =>
            PieceKind(c.toString.toInt)
        }
        //println(pieces.toSeq)
        val s0 = newState(Nil, (10, 23), pieces.toSeq)
        var s: GameState = s0
        var i: Int = 0
        val agent = new Agent
        while ((s.status == ActiveStatus) && (i < 100)) {
            val ms = agent.bestMoves(s)
            s = Function.chain(ms map {toTrans})(s)
            i = i + 1
        }
        printState(s)
        println(file.getName + ": " + s.lineCount.toString)
    }
    def generateScript: String =
     (Stage.randomStream(new util.Random) map {
         _.toInt.toString} take {1000}) mkString

    def printState(s: GameState) {
        val poss = s.blocks map {_.pos}
        (0 to s.gridSize._2 - 1).reverse foreach { y =>
            (0 to s.gridSize._1 - 1) foreach { x =>
                if (poss contains (x, y)) print("x")
                else print(" ")
            }
            println("")
            if (y == 0) (0 to s.gridSize._1 - 1) foreach { x =>
                print("-") }
        }
        println("")
    }
}