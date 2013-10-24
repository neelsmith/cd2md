
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestCitedImg extends GroovyTestCase {

    String src = """
Illusration of folio 12 recto, {image in natural light}[1]

[1]: urn:cite:hmt:vaimg.VA012RN-0013
"""
    String expectedMarkdown = """
Illusration of folio 12 recto, [image in natural light][1]

[1]: http://beta.hpcc.uh.edu/tomcat/hmtcite/images?request=GetImagePlus&urn=urn:cite:hmt:vaimg.VA012RN-0013
"""

    @Test void testCitation() {
        CitedownToMarkdown c2m = new CitedownToMarkdown()
        assert  c2m.toMarkdown(src) == expectedMarkdown
    }

}
