package thegame

import org.scalajs.dom.CanvasRenderingContext2D

trait TheGame {

  class State(windowSize: Vect) {
    val unitSize = windowSize.y / 40
    var paddleHeight = unitSize * 7
    var paddleMiddle = windowSize.y / 2
  }

  var state: State = _

  def init(windowSize: Vect): Unit = {
    state = new State(windowSize)
  }

  def update(): Unit = {

  }

  def draw(ctx: CanvasRenderingContext2D): Unit = {

  }
}
