
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestCitedObj extends GroovyTestCase {

    
    String cdsource = """
Folio {twelve recto}[1].

[1]: urn:cite:hmt:msA.12r
"""

    String expectedMarkdown = """
Folio [twelve recto][1].

[1]: http://www.homermultitext.org/hmt-digital/collections?request=GetObjectPlus&urn=urn:cite:hmt:msA.12r
"""

    @Test void testCitation() {
        CitedownConverter c2m = new CitedownConverter()
        assert c2m.toMarkdown(cdsource) == expectedMarkdown
    }

}
