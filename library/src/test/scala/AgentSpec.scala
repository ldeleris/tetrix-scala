import org.specs2._

class AgentSpec extends Specification { def is =            s2"""
    This is a specification to check Agent

    Utility function should
        evaluate initial state as 0.0.                      $utility1
        evaluate GameOver as -1000.0.                       $utility2
        evaluate an active state by lineCount               $utility3

    Solver should
        pick MoveLeft for s1                                $solver1
        pick Drop for s3                                    $solver2
                                                              """   

    import com.deleris.tetrix._
    import Stage._

    val agent = new Agent
    def s1 = newState(Block((0, 0), TKind) :: Nil, (10, 20), Nil padTo (20, TKind))
    def s3 = newState(Seq(
        (0, 0), (1, 0), (2, 0), (3, 0), (7, 0), (8, 0), (9, 0))
        map { Block(_, TKind) }, (10, 20), Nil padTo (20, TKind))
    def gameOverState = Function.chain(Nil padTo (10, drop))(s1)

    def utility1 =
        agent.utility(s1) must_== 0.0

    def utility2 =
        agent.utility(gameOverState) must_== -1000.0
    
    def utility3 = {
        val s = Function.chain(Nil padTo (19, tick))(s3)
        agent.utility(s) must_== 1.0
    }
    
    def solver1 =
        agent.bestMove(s1) must_== MoveLeft

    def solver2 =
        agent.bestMove(s3) must_== Drop
}