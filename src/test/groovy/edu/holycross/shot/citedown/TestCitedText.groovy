
package edu.holycross.shot.citedown


import static org.junit.Assert.*
import org.junit.Test

class TestCitedText extends GroovyTestCase {

    
    String cdsource = """
{*Iliad* 1.1}[1]

[1]: urn:cts:greekLit:tlg0012.tlg001.msA:1.1
"""

    String expectedMarkdown = """
[*Iliad* 1.1][1]

[1]: http://www.homermultitext.org/hmt-digital/texts?request=GetPassagePlus&urn=urn:cts:greekLit:tlg0012.tlg001.msA:1.1
"""

    @Test void testCitation() {
        CitedownConverter c2m = new CitedownConverter()
	c2m.debug = 5
        assert c2m.toMarkdown(cdsource) == expectedMarkdown

    }

}
