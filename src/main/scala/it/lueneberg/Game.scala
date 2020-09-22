package it.lueneberg

import it.lueneberg._

class Game (p1: Player, p2: Player, iterations: Int) {

  import it.lueneberg.GameResult.{given _}

  def play(): GameResult =
    LazyList.continually((p1.nextAction(), p2.nextAction()))
      .take(iterations)
      .foldLeft(GameResultMonoid.empty) { case (agg, (a1, a2)) =>
        agg.combine(a1.evaluateToGameResult(a2))
      }

}

