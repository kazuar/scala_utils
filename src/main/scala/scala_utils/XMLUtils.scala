package scala_utils

import java.io.ByteArrayInputStream
import javax.xml.stream.events.XMLEvent
import javax.xml.stream.events.StartElement
import javax.xml.stream.{XMLInputFactory, XMLStreamException}

/**
  * Created by vidas on 5/14/16.
  */
object XMLUtils {

  object AllDone extends Exception { }

  def xmlFixer(xml: String): String = {
    val byteArray = xml.getBytes("UTF-8")
    val inputStream = new ByteArrayInputStream(byteArray)
    val inputFactory = XMLInputFactory.newInstance()

    val reader = inputFactory.createXMLEventReader(inputStream)

    var stack = List.empty[StartElement]
//    val stack = scala.collection.immutable.Stack.empty

    var removeTags = List.empty[StartElement]

    try {
      while (reader.hasNext) {
        try {
          val event = reader.nextEvent()
          if (event.isStartElement()) {
            val startElement = event.asStartElement()
            println("processing element: " + startElement.getName().getLocalPart())
            stack = startElement :: stack
            //          val pushStack = stack.push(startElement)
          }
          if (event.isEndElement()) {
            stack = stack.tail
            //          stack.pop
          }
        } catch {
          case e: XMLStreamException =>
            e.getLocation.getCharacterOffset
            System.out.println("error in line: " + e.getLocation.getLineNumber())
            //          val se = stack.pop.asInstanceOf[StartElement]
            if (stack.nonEmpty) {
              val se = stack.head
              removeTags = se :: removeTags
              stack = stack.tail
              System.out.println("non-closed tag:" + se.getName.getLocalPart() + " " + se.getLocation.getLineNumber())
            }
          case e: Exception => throw AllDone
          //        throw e;
        }
      }
    } catch {
      case AllDone =>
    }

    val newTxt = new StringBuilder(xml)
    removeTags.foreach(tag => {
      val start = tag.getLocation.getColumnNumber - tag.toString.length - 1
      val end = start + tag.toString.length
      newTxt.replace(start, end, " ")
    })
    println(xml)
    println(newTxt.toString)
    newTxt.toString
  }
}
