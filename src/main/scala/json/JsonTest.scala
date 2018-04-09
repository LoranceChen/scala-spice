package json

import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.json.Serialization.{read, write}

/**
  *
  */
object JsonTest extends App {
  implicit val formats = DefaultFormats

  val jsonStr =
    """
      |{
      |  "\": 1
      |}
    """.stripMargin

  val key = """\"""
  println(key)
  val json2: JObject = key -> "value"
  println(json2)

  println(write(json2))

}

object JsonTest2 extends App {
  implicit val formats = DefaultFormats

  val key = "\n"
  println(key)
  val json2: JObject = key -> "value"
  println(write(json2))

}
