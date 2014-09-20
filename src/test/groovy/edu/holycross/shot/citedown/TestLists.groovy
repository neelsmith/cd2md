
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestLists extends GroovyTestCase {

  CitedownConverter c2m = new CitedownConverter()

  @Test void testUl() {
    String ul = """
Unordered list.

- item the first
- second thing
- thing 3

and subsequent paragraph.

"""
    assert c2m.toMarkdown(ul) == ul
  }



  @Test void testOrdered() {
    String ol = """
Ordered list.

1. item the first
1. second thing
1. thing 3

and subsequent paragraph.

"""
    assert c2m.toMarkdown(ol) == ol
  }




  @Test void testInternalMarkup() {
    String ulwEmph = """
Unordered list.

- item *the first*
- second thing

and subsequent paragraph.

"""
    assert c2m.toMarkdown(ulwEmph) == ulwEmph
  }




}
