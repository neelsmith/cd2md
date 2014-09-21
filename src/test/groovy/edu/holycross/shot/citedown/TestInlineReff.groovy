
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestInlineReff extends GroovyTestCase {

  String docSrc = """
Here's a {ref to text}[il1_1]. Embedded image follows.

!{iliad image}[img]

Reff are at end.

[il1_1]: urn:cts:greekLit:tlg0012.tlg001.msA:1.1

[img]: urn:cite:hmt:vaimg.VA012RN-0013@0.2002,0.211,0.1732,0.018

"""

  CitedownConverter c2m = new CitedownConverter()


  @Test void testMultiInline() {
    c2m.debug = 5
    System.err.println c2m.toMarkdown(docSrc)
  }

}
