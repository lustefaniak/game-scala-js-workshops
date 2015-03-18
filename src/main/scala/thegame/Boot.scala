package thegame

import org.scalajs.dom
import org.scalajs.dom._

import scala.scalajs.js.annotation.JSExport

@JSExport
object Boot extends TheGame {

  @JSExport
  def main(canvas: html.Canvas): Unit = {

    init(resizeCanvas(canvas))
    
    val ctx = canvas.getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]

    def clear() = {
      ctx.fillStyle = "black"
      ctx.fillRect(0, 0, canvas.width, canvas.height)
    }

    def run = {
      clear()
      update()
      draw(ctx)
    }

    dom.setInterval(() => run, 32)
  }

  def resizeCanvas(canvas: html.Canvas) = {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
    Vect(canvas.width, canvas.height)
  }

}
