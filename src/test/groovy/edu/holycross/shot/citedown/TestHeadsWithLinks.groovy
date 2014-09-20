
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestHeadsWithLinks extends GroovyTestCase {

  CitedownConverter c2m = new CitedownConverter()





  @Test void testLink() {
    String pgwLink = """# Page header #

Paragraph {linking to Iliad 1.1}[ref1],
in the middle of a paragraph.

[ref1]: urn:cts:greekLit:tlg0012.tlg001.msA:1.1

"""

String expectedMd = """# Page header #

Paragraph [linking to Iliad 1.1][ref1],
in the middle of a paragraph.

[ref1]: http://www.homermultitext.org/hmt-digital/texts?request=GetPassagePlus&urn=urn:cts:greekLit:tlg0012.tlg001.msA:1.1
"""

assert c2m.toMarkdown(pgwLink)  == expectedMd
  }



}
