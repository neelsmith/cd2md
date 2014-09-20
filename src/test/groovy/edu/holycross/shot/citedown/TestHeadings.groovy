
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestHeadings extends GroovyTestCase {

    String src = """
# First-level heading #

##Second-level *emphasised* heading##

Paragraph of text.

"""

@Test void testHeadings() {
  CitedownConverter c2m = new CitedownConverter()
  assert c2m.toMarkdown(src) == src
}

}
