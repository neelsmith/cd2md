
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

[1]: http://beta.hpcc.uh.edu/tomcat/hmtcite/texts?request=GetPassagePlus&urn=urn:cts:greekLit:tlg0012.tlg001.msA:1.1
"""

    @Test void testCitation() {
        CitedownToMarkdown c2m = new CitedownToMarkdown()
        System.err.println  c2m.toMarkdown(cdsource)
    }

}
