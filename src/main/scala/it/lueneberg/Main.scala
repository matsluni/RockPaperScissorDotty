package it.lueneberg

import scala.util.Random

object Main {

  def main(args: Array[String]): Unit = {
    val game = new Game(Player(() => Action.Paper), Player(() => Action.all(Random.nextInt(Action.size))), 100)
    val gameResult = game.play()
    gameResult.print()
  }

}
