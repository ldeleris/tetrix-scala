import org.specs2._

class AgentSpec extends Specification { def is =            s2"""
    This is a specification to check Agent

    Utility function should
        evaluate initial state as 0.0.                      $utility1
        evaluate GameOver as -1000.0.                       $utility2
        evaluate an active state by lineCount               $utility3
        penalyse having gaps between the columns            $utility4

    Solver should
        pick MoveLeft for s1                                $solver1
        pick Drop or Tick for s3                            $solver2
        pick RotateCW for s5                                $solver3

    Penalty function should
        penalize having blocks stacked up high              $penalty1

    ActionSeqs function should
        list out potential action sequences                 $actionSeq1
                                                              """   

    import com.deleris.tetrix._
    import Stage._

    val agent = new Agent
    def s0 = newState(Nil, (10, 20),  Nil padTo (20, TKind))
    def s1 = newState(Block((0, 0), TKind) :: Nil, (10, 20), Nil padTo (20, TKind))
    def s3 = newState(Seq(
        (0, 0), (1, 0), (2, 0), (3, 0), (7, 0), (8, 0), (9, 0))
        map { Block(_, TKind) }, (10, 20), Nil padTo (20, TKind))
    def s5 = newState(Seq(
        (0, 0), (1, 0), (2, 0), (3, 0), (4, 0), (5, 0), (6, 0), (7, 0), (9, 0))
        map { Block(_, TKind) }, (10, 20), Nil padTo (20, TKind))
    def gameOverState = Function.chain(Nil padTo (10, drop))(s1)

    def utility1 =
        agent.utility(s0) must_== 0.0

    def utility2 =
        agent.utility(gameOverState) must_== -1000.0
    
    def utility3 = {
        val s = Function.chain(Nil padTo (19, tick))(s3)
        agent.reward(s) must_== 1.0
    }

    def utility4 = {
        val s = newState(Seq(
            (0, 0), (0, 1), (0, 2), (0, 3), (0, 4), (0, 5), (0, 6))
            map { Block(_, TKind) }, (10, 20), TKind :: TKind :: Nil)
        agent.utility(s) must_== -4.9
    } and {
        val s = newState(Seq((1, 0), (1, 1), (2, 1), (2, 2))
            map { Block(_, ZKind) }, (10, 20), TKind :: TKind :: Nil)
        agent.utility(s) must_== -1.3
    }
    
    def solver1 =
        agent.bestMove(s1) must_== MoveLeft

    def solver2 =
        agent.bestMove(s3) must beOneOf(Drop, Tick)

    def solver3 =
        agent.bestMove(s5) must_== RotateCW

    def penalty1 = {
        val s = newState(Seq(
            (0, 0), (0, 1), (0, 2), (0, 3), (0, 4), (0, 5), (0, 6))
            map { Block(_, TKind) }, (10, 20), TKind :: TKind :: Nil)
        agent.penalty(s) must_== 49.00
    } and {
        val s = newState(Seq((1, 0))
            map { Block(_, ZKind) }, (10, 20), TKind :: TKind :: Nil)
        agent.penalty(s) must_== 1.0
    }

    def actionSeq1 = {
        val s = newState(Nil, (10, 20), TKind :: TKind :: Nil)
        val seqs = agent.actionSeqs(s)
        seqs.size must_== 32
    }
}