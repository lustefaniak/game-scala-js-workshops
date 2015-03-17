package thegame

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.window
import org.scalajs.dom.console

import scala.scalajs.js


trait TheGame {

  val keys = collection.mutable.Set[Int]()

  window.onkeydown = (kd: KeyboardEvent) => {
    keys.add(kd.keyCode)
  }

  window.onkeyup = (kd: KeyboardEvent) => {
    keys.remove(kd.keyCode)
  }

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
    keys.foreach(console.log(_))
    console.log(state.asInstanceOf[js.Any])


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
