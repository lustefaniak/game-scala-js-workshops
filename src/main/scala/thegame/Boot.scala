package thegame

import org.scalajs.dom.document
import org.scalajs.dom.console
import org.scalajs.dom.ext.KeyCode

import org.scalajs.dom.raw.{KeyboardEvent, Event, HTMLInputElement, HTMLDivElement}

import scala.scalajs.js
import scalatags.JsDom.all._
import rx._
import scalatags.rx.ext._
import scalatags.rx.all._

import scala.scalajs.js.annotation.JSExport

case class Task(what: String, done: Var[Boolean] = Var(false))

@JSExport
object Boot {

  var container: HTMLDivElement = _

  val tasks = Var(List[Task](
    Task("Get out thrash"),
    Task("Pet the dog", Var(true))
  ))

  //  val s = new {
  //    var internal:String = ""
  //    def update(newS:String): Unit ={
  //      internal = newS
  //    }
  //  }
  //  s() = "test"


  val todoInput = input(
    onkeyup := ((e: KeyboardEvent) => {
      if (e.keyCode == KeyCode.enter) {
        val me = e.target.asInstanceOf[HTMLInputElement]
        val what = me.value
        tasks() ::= Task(what)
        me.value = ""
      }
    }),
    autofocus := "true"
  )

  @JSExport
  def main(cont: HTMLDivElement): Unit = {
    container = cont

    val todo =
      div(
        todoInput,
        Rx {
          ul(
            tasks().map(t => {
              Rx {
                li(
                  input(
                    `type` := "checkbox",
                    if (t.done()) (checked := "true") else (),
                    onclick := (() => {
                      t.done() = !t.done()
                    })
                  ),
                  style := (if (t.done()) "text-decoration: line-through;" else ""),
                  t.what
                ).render
              }
            })
          ).render
        }
      )

    container.innerHTML = ""
    container.appendChild(todo.render)

  }
}
