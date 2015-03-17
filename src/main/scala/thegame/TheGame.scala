package thegame

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.window
import org.scalajs.dom.console

import scala.scalajs.js

case class CollisionPlane(vect: Vect, d: Double, isPaddle: Boolean = false)

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
    var ballDirection = Vect(3, 1).normalize
    var ballSpeed = 10
    var paddleSpeed = 30
    var points = 0
    val collisionPlanes: List[CollisionPlane] = List(
      CollisionPlane(Vect(1, 0), unitSize * -2, true),
      CollisionPlane(Vect(0, 1), unitSize * -2),
      CollisionPlane(Vect(0, -1), windowSize.y - unitSize * 3),
      CollisionPlane(Vect(-1, 0), windowSize.x - unitSize * 3)
    )
    var gameOver = false
  }

  var state: State = _

  def init(windowSize: Vect): Unit = {
    state = new State(windowSize)
  }

  def vectorReflection(vect: Vect, normal: Vect): Vect = {
    val dot: Double = normal.dot(vect) * -2
    val reflection: Vect = normal * dot
    (vect + reflection).normalize
  }

  def isTouchingPadle(pos: Vect): Boolean = {
    pos.y > state.paddleMiddle - state.paddleHeight / 2 && pos.y < state.paddleMiddle + state.paddleHeight + state.unitSize
  }

  def update(): Unit = {
    if (state.ballPosition.x < state.unitSize
      || state.ballPosition.x > state.windowSize.x - state.unitSize
      || state.ballPosition.y < state.unitSize
      || state.ballPosition.y > state.windowSize.y - state.unitSize) {
      state.gameOver = true
    } else {

      keys.foreach(console.log(_))
      console.log(state.asInstanceOf[js.Any])
      if (keys(KeyCode.w)) {
        state.paddleMiddle -= state.paddleSpeed
      } else if (keys(KeyCode.s)) {
        state.paddleMiddle += state.paddleSpeed
      }

      state.paddleMiddle = Math.min(Math.max(state.paddleHeight / 2 + state.unitSize * 3, state.paddleMiddle), state.windowSize.y - state.paddleHeight / 2 - state.unitSize * 3)

      var newPos = state.ballPosition + state.ballDirection * state.ballSpeed
      var newDir = state.ballDirection
      state.collisionPlanes.foreach {
        case CollisionPlane(normal, d, isPaddle) =>
          val distanceToPlane = newPos.dot(normal) + d
          if (distanceToPlane < 0) {
            if (!isPaddle || isTouchingPadle(newPos)) {
              newDir = vectorReflection(newDir, normal)
              newPos = newPos - (newDir * distanceToPlane)
              if(isPaddle) state.points += 1
            }
          }
      }

      state.ballPosition = newPos
      state.ballDirection = newDir

    }
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
