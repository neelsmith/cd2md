
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestMultiReff extends GroovyTestCase {

  String reff = """
I like putting reff at the end.

[il1_1]: urn:cts:greekLit:tlg0012.tlg001.msA:1.1

[img]: urn:cite:hmt:vaimg.VA012RN-0013@0.2002,0.211,0.1732,0.018

"""

  CitedownConverter c2m = new CitedownConverter()

  @Test void testReff() {
    String expectedReff = """
I like putting reff at the end.

[il1_1]: http://www.homermultitext.org/hmt-digital/texts?request=GetPassagePlus&urn=urn:cts:greekLit:tlg0012.tlg001.msA:1.1

[img]: http://www.homermultitext.org/hmt-digital/images?request=GetImagePlus&urn=urn:cite:hmt:vaimg.VA012RN-0013@0.2002,0.211,0.1732,0.018

"""
    assert  c2m.toMarkdown(reff) == expectedReff
  }


}
