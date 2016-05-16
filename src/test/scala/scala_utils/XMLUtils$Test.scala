package scala_utils

import org.scalatest.{FlatSpec, MustMatchers}

/**
  * Created by vidas on 5/14/16.
  */
class XMLUtils$Test extends FlatSpec with MustMatchers {

  behavior of "XMLUtils$Test"

  it must "return the same xml" in {
    val t = "<a></a>"
    XMLUtils.xmlFixer(t) must equal(t)
  }

  it must "remove unclosed tags" in {
    val t = "<a><br></a>"
    XMLUtils.xmlFixer(t) must equal("<a> </a>")
  }

}
