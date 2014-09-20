
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestListsWithLinks extends GroovyTestCase {

  CitedownConverter c2m = new CitedownConverter()





  @Test void testLink() {
    String ulwLink = """
Unordered list.

- item {linking to Iliad 1.1}[ref1]
- second thing

and subsequent paragraph.

[ref1]: urn:cts:greekLit:tlg0012.tlg001.msA:1.1

"""

    c2m.debug = 5
    System.err.println c2m.toMarkdown(ulwLink) 
  }



}
