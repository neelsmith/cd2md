
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestLists extends GroovyTestCase {

  CitedownConverter c2m = new CitedownConverter()

  @Test void testEmph() {
    String ul = """
Unordered list.

- item the first
- second thing
- thing 3

and subsequent paragraph.

"""
    System.err.println c2m.toMarkdown(ul)
    assert c2m.toMarkdown(ul) == ul
  
  }
}
