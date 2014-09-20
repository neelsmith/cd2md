
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestCitedImg extends GroovyTestCase {

    String src = """
Illustration of folio 12 recto, {image in natural light}[1]

[1]: urn:cite:hmt:vaimg.VA012RN-0013
"""
    String expectedMarkdown = """
Illustration of folio 12 recto, [image in natural light][1]

[1]: http://www.homermultitext.org/hmt-digital/images?request=GetImagePlus&urn=urn:cite:hmt:vaimg.VA012RN-0013
"""

    @Test void testCitation() {
        CitedownConverter c2m = new CitedownConverter()
        assert  c2m.toMarkdown(src) == expectedMarkdown
    }

}
