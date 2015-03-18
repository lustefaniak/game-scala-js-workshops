package thegame

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.console
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.window
import rx._

case class CP(vect:Vect, d:Double, isPaddle:Boolean = false)

trait TheGame {

  val keys = collection.mutable.Set[Int]()

  window.onkeydown = (ke: KeyboardEvent) => {
    keys.add(ke.keyCode)
  }

  window.onkeyup = (ke: KeyboardEvent) => {
    keys.remove(ke.keyCode)
  }

  class State(val windowSize: Vect) {
    val unitSize = windowSize.y / 30.0
    var ballPosition: Vect = Vect(windowSize.x / 2, windowSize.y / 2)
    var ballDirection = Vect(3, 1).normalize
    var points = Var(0)
    val ballSpeed = Rx { unitSize / 2.0 * Math.pow(1.04, points()) }
    val paddleHeight = unitSize * 5.0
    var paddleTop = windowSize.y / 2 - paddleHeight / 2
    val paddleSpeed = unitSize
    val collisionPlanes: List[CP] = List(
      CP(Vect(-1, 0), windowSize.x - unitSize),
      CP(Vect(0, 1), 0.0),
      CP(Vect(0, -1), windowSize.y - unitSize),
      CP(Vect(1, 0), -unitSize*2, true)
    )
  }

  var state: State = _

  def init(windowSize: Vect): Unit = {
    state = new State(windowSize)
  }

  def reflection(vect: Vect, normal: Vect): Vect = {
    val dot = normal.dot(vect) * -2
    val reflection = normal * dot
    (vect + reflection).normalize
  }

  def isTouchingPaddle:Boolean = true

  def update(): Unit = {

    if (keys(KeyCode.r)) {
      state = new State(state.windowSize)
    }

    if (keys(KeyCode.down)) {
      state.paddleTop += state.paddleSpeed
    }
    if (keys(KeyCode.up)) {
      state.paddleTop -= state.paddleSpeed
    }

    state.paddleTop = Math.min(
      Math.max(state.paddleTop, 0),
      state.windowSize.y - state.paddleHeight)

    var newPos = state.ballPosition + state.ballDirection * state.ballSpeed()
    var newDir = state.ballDirection
    state.collisionPlanes.foreach {
      case CP(normal, d, isPaddle) =>
        val distanceFromPlane = normal.dot(newPos) + d
        if (distanceFromPlane <= 0) {
          if(!isPaddle || isTouchingPaddle) {
            newDir = reflection(newDir, normal)
            newPos = newPos + newDir * -distanceFromPlane
          }

          if(isPaddle) {
            state.points() += 1
          }

        }
    }

    state.ballDirection = newDir
    state.ballPosition = newPos


  }

  def draw(ctx: CanvasRenderingContext2D): Unit = {
    ctx.fillStyle = "white"
    ctx.fillRect(state.unitSize, state.paddleTop, state.unitSize, state.paddleHeight)

    ctx.fillStyle = "red"
    ctx.fillRect(state.ballPosition.x, state.ballPosition.y, state.unitSize, state.unitSize)

    ctx.font = (state.unitSize * 2).toInt + "px Arial"
    ctx.fillText(state.points().toString, state.windowSize.x/2, state.unitSize * 3, state.unitSize)

  }
}
