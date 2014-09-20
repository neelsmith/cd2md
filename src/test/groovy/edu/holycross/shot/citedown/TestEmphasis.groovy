
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestEmphasis extends GroovyTestCase {

  CitedownConverter c2m = new CitedownConverter()

  @Test void testEmph() {
    String emph = "Phrase with *emphatic* substring."
    assert c2m.toMarkdown(emph) == emph
  
  }


  @Test void testStrong() {
    String strong = "Phrase with **strong** emphasis."
    assert c2m.toMarkdown(strong) == strong
  }

  @Test void testMixed() {
    String mixed = "Phrase with both *emphasized* and **strongly** emphasized substrings."
    //    assert c2m.toMarkdown(mixed) == mixed
    c2m.debug = 5
    System.err.println c2m.toMarkdown(mixed) 
  }

}
