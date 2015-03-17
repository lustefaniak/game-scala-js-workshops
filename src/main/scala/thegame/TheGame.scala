package thegame

import org.scalajs.dom.CanvasRenderingContext2D

trait TheGame {

  class State(val windowSize: Vect) {
    val unitSize = windowSize.y / 40
    var paddleHeight = unitSize * 7
    var paddleMiddle = windowSize.y / 2
    var ballPosition = windowSize * 0.5

  }

  var state: State = _

  def init(windowSize: Vect): Unit = {
    state = new State(windowSize)
  }

  def update(): Unit = {

  }

  def draw(ctx: CanvasRenderingContext2D): Unit = {

    ctx.fillStyle = "white"
    ctx.fillRect(state.unitSize, state.paddleMiddle - state.paddleHeight / 2, state.unitSize, state.paddleHeight)

    ctx.fillRect(state.unitSize, state.unitSize, state.windowSize.x - state.unitSize * 2, state.unitSize)
    ctx.fillRect(state.unitSize, state.windowSize.y - state.unitSize * 2, state.windowSize.x - state.unitSize * 2, state.unitSize)

    ctx.fillStyle = "blue"
    ctx.fillRect(state.windowSize.x - state.unitSize * 2, state.unitSize * 2, state.unitSize, state.windowSize.y - state.unitSize * 4)

    ctx.fillStyle = "gray"
    ctx.fillRect(state.ballPosition.x, state.ballPosition.y, state.unitSize, state.unitSize)

  }
}
