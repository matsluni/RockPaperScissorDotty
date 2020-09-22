package it.lueneberg

import java.io.{PrintStream, Serializable}

enum Result {
  case WIN, DRAW, LOSE
}

sealed trait Action extends Product with Serializable {
  def beats: Set[Action]
  def evaluate(other: Action): Result =
    if (this == other) Result.DRAW
    else {
      if (beats.contains(other)) Result.WIN
      else Result.LOSE
    }

  def evaluateToGameResult(other: Action): GameResult =
    GameResult.fromResult(evaluate(other))
}
object Action {
  val all = List(Action.Rock, Action.Paper, Action.Scissor)
  val size = all.size
  case object Rock extends Action {
    override def beats: Set[Action] = Set(Scissor)
  }
  case object Scissor extends Action {
    override def beats: Set[Action] = Set(Paper)
  }
  case object Paper extends Action {
    override def beats: Set[Action] = Set(Rock)
  }
}

case class GameResult(p1Wins: Int, p1Loses: Int, p1Draws: Int) {
  val p2Wins = p1Loses
  val p2Draws = p1Draws
  val p2Loses = p1Wins
  def print(printStream: PrintStream = System.out): Unit = {
    printStream.println(
      s"""
          Game result
          ###########
          
          Player 1 Wins : $p1Wins
          Player 1 Draws: $p1Draws
          Player 1 Loses: $p1Loses
  
          Player 2 Wins : $p2Wins
          Player 2 Draws: $p2Draws
          Player 2 Loses: $p2Loses
          """
    )

  }
}
object GameResult {
  def fromResult(res: Result): GameResult = res match {
    case Result.WIN => GameResult(1, 0, 0 )
    case Result.LOSE => GameResult(0, 1, 0 )
    case Result.DRAW => GameResult(0, 0, 1 )
  }

  given GameResultMonoid as Monoid[GameResult] {
    extension (x: GameResult) def combine (y: GameResult): GameResult = 
      GameResult(x.p1Wins + y.p1Wins, x.p1Loses + y.p1Loses, x.p1Draws + y.p1Draws)
    def empty: GameResult = GameResult(0, 0, 0)
  }
}

case class Player(private val f: () => Action) {
  def nextAction(): Action = f()
}
