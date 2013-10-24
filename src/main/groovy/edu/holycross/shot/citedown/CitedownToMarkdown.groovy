/**
Class to convert citedown material to pure markdown.
*/

package edu.holycross.shot.citedown

import edu.harvard.chs.citedown.Extensions
import edu.harvard.chs.citedown.PegDownProcessor
import edu.harvard.chs.citedown.ast.*
import org.parboiled.support.ParsingResult

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn


class CitedownToMarkdown {


    int debug = 0

    /** Result from parsing citedown source, set in constructor. */
    ParsingResult pr

    /** Map of reference labels to source values. */
    def refMap = [:]

    // Configurable run-time settings:

    /** Base URL for CTS request. */
    String cts = "http://beta.hpcc.uh.edu/tomcat/hmtcite/texts?request=GetPassagePlus&"

    /** Base URL for CITE Collection request. */
    String coll = "http://beta.hpcc.uh.edu/tomcat/hmtcite/collections?request=GetObjectPlus&"

    /** Base URL for CITE Collection request. */
    String img = "http://beta.hpcc.uh.edu/tomcat/hmtcite/images?request=GetImagePlus&"

    /** List of collections configured with CITE Image Extension. */
    def imgCollections = ['urn:cite:hmt:vaimg', 'urn:cite:hmt:vbimg', 'urn:cite:hmt:u4img','urn:cite:hmt:e3img', 'urn:cite:hmt:e4img']


    


    /** Constructor naming three base URLs */
    CitedownToMarkdown(String ctsBase, String collBase, String imgBase) {
        this.cts = ctsBase
        this.coll = collBase
        this.img = imgBase
    }

    /** Empty constructor */
    CitedownToMarkdown() {
    }

    /** Constructor working with String input.
    * @param citedownText String with citedown content.
    * @throws Exception if unable to parse citedown.
    */
    CitedownToMarkdown(String citedownText) 
    throws Exception {
        PegDownProcessor pdp = new PegDownProcessor(Extensions.CITE)
        try {
            this.pr = pdp.parser.parseToParsingResult(citedownText.toCharArray())
        } catch (Exception e) {
            throw new Exception("CitedownToMarkdown: unable to parse source  text;  ${e}")
        }

    }

    /** Constructor working with file input.
    * @param citedownFile File with citedown source.
    * @throws Exception if unable to parse citedownFile.
    */
    CitedownToMarkdown(File citedownFile) 
    throws Exception {
        String citedownText = citedownFile.getText("UTF-8")
        PegDownProcessor pdp = new PegDownProcessor(Extensions.CITE)
        try {
            this.pr = pdp.parser.parseToParsingResult(citedownText.toCharArray())
        } catch (Exception e) {
            throw new Exception("CitedownToMarkdown: unable to parse file ${citedownFile};  ${e}")
        }
    }


    /** Resolves a CITE URN value to a URL.  If the URN value
    * is a CITE URN, the list of collections configured with the
    * CITE Image Extension is checked.
    * @param urnStr String value of the URN to resolve.
    * @returns A URL pointing to the CITE *Plus method of
    * the appropriate CITE service for the URN.
    * @throws Exception if urnStr cannot be parsed as
    * either a CTS URN or a CITE Collection URN.
    */
    String urlForUrn(String urnStr) 
    throws Exception {
        if (debug > 0) { System.err.println "Test " + urnStr} 
        String reply = null
        try {
            CtsUrn urn = new CtsUrn(urnStr)
            reply = "${cts}urn=${urn}"
            if (debug  > 0) { System.err.println "${urn} is a CTS URN."}
        } catch (Exception ctse) {
        }

        try {
            CiteUrn urn = new CiteUrn(urnStr)
            String collectionUrn = "urn:cite:${urn.getNs()}:${urn.getCollection()}"
            if (imgCollections.contains(collectionUrn) ) {
                reply = "${img}urn=${urn}"
            } else {
                reply = "${coll}urn=${urn}"
            }
        } catch (Exception obje) {
        }
        if (reply == null) {
            throw new Exception ("CitedownToMarkdown:  could not resolve URN ${urnStr} to a URL.")
        } else {
            return reply
        }
    }



    String toMarkdown(File f) {
        String citedownText = f.getText("UTF-8")
        PegDownProcessor pdp = new PegDownProcessor(Extensions.CITE)
        try {
            this.pr = pdp.parser.parseToParsingResult(citedownText.toCharArray())
            this.toMarkdown()
        } catch (Exception e) {
            throw new Exception("CitedownToMarkdown: unable to parse file ${f};  ${e}")
        }
    }

    /** Converts content of parsed citedown stored in this.pr to pure markdown.
    * First constructs a map of reference labels to source URNs,
    * then walks the parsed tree to convert content to markdown.
    * @returns A String of markdown.
    */
    String toMarkdown() {
        collectReff()
        return collectNodes()
    }


    /** Finds a pair of Label and RefSrc elements in the parse tree, and stores
    * them in refMap.  Recursively walks from node until it encounters both a 
    * Label and RefSrc element.
    * @param node Node to descend from in searching for Label and RefSrc elements.
    * @param buff Original parboiled input buffer from which we can extract appropriate
    * String values for key and ref.
    * @param key The String value of the Label element, used as key to a refMap entry.
    * @param ref The String value of the RefSrc element.
    * @param done Flag true if both key and ref have been found.
    */
    void extractRef(Object node, Object buff, String key, String ref, boolean done) {
        node.getChildren().each { ch ->
            switch (ch.getLabel()) {
                case "Label":
                    key = buff.extract(ch.getStartIndex(), ch.getEndIndex())
                if (ref.size() > 0) {
                    this.refMap[key] = ref
                    done = true
                }
                break

                case "RefSrc":
                    ref = buff.extract(ch.getStartIndex(), ch.getEndIndex())
                if (key.size() > 0) {
                    this.refMap[key] = ref
                    done = true
                }
                break
            
                default : 
                break
            }
            if (! done) {
                extractRef(ch, buff, key, ref, done)
            }

        }
    }

    /** Collects all reference definitions from source and stores them
    * in refMap.  Recursively walks the parse tree until in finds "Reference"
    * elements, and extracts Label-SrcRef pairs from each "Reference" element.
    * With no arguments, defaults to using root of parse tree as starting
    * point for recursive descent.
    */
    void collectReff() {
        def root = pr.parseTreeRoot
        collectReff(root, pr.inputBuffer)
    }

    /** Collects all reference definitions from source and stores them
    * in refMap.  Recursively walks the parse tree until in finds "Reference"
    * elements, and extracts Label-SrcRef pairs from each "Reference" element.
    * @param node Node to descend from in searching for Reference elements.
    * @param buff Original parboiled input buffer from which we can extract appropriate
    * String values.
    */
    void collectReff(Object node, Object inBuff) {
        node.getChildren().each { ch ->
            switch (ch.getLabel()) {
                case "Reference":
                    extractRef(ch,inBuff, "", "", false)
                break
                default : 
                    break
            }
            collectReff(ch, inBuff)
        }
    }


    /** Finds values for Label and RefSrc within a Reference element.
    * @param node The node to search for CiteLabel and CiteReferenceLink elements.
    * @buff The underlying parboiled input buffer from which we can extract String values.
    * @key The value of the CiteLabel element, when found.
    * @ref The value of the CiteReferenceLink element, when found.
    * @reply A String containing the markdown equivalent of the Citedown Reference,
    * with fully resolved URLs.
    * @returns  A String containing the markdown equivalent of the Citedown Reference,
    * with fully resolved URLs.
    */
    String reformatReference(Object node, Object buff, String key, String ref, String reply, boolean done) {
        node.getChildren().each { ch ->
            switch (ch.getLabel()) {
                case "Label":
                    key = buff.extract(ch.getStartIndex(), ch.getEndIndex())
                if (ref.size() > 0) {
                    reply = "${key}: ${urlForUrn(ref)}\n"
                    done = true
                }
                break

                case "RefSrc":
                    ref = buff.extract(ch.getStartIndex(), ch.getEndIndex())
                if (key.size() > 0) {
                    reply = "${key}: ${urlForUrn(ref)}\n"
                    done = true
                }
                break
            
                default : 
                break
            }
            if (! done) {
                reply = reformatReference(ch, buff, key, ref, reply, done)
            }
        }
        return reply
    }

    /** Finds values for CiteLabel and CiteReferenceLink within a Link element.
    * @param node The node to search for CiteLabel and CiteReferenceLink elements.
    * @buff The underlying parboiled input buffer from which we can extract String values.
    * @key The value of the CiteLabel element, when found.
    * @ref The value of the CiteReferenceLink element, when found.
    * @reply A list containing, in sequence, the key and ref values.
    * @returns A list containing, in sequence, the key and ref values.
    */
    ArrayList extractCiteRef(Object node, Object buff, String key, String ref, ArrayList reply, boolean done) {
        node.getChildren().each { ch ->
            switch (ch.getLabel()) {
                case "CiteLabel":
                    key = buff.extract(ch.getStartIndex() + 1, ch.getEndIndex() - 1)
                if (ref.size() > 0) {
                    reply.add(key)
                    reply.add(ref)
                    if (debug > 0) { System.err.println "${key} mapped to ${ref}"}
                    done = true
                }
                break

                case "CiteReferenceLink":
                    ref = buff.extract(ch.getStartIndex(), ch.getEndIndex())
                if (key.size() > 0) {
                    reply.add(key)
                    reply.add(ref)
                    if (debug > 0) { System.err.println "${key} mapped to ${ref}"}
                    done = true
                }
                break
            
                default : 
                break
            }
            if (! done) {
                reply = extractCiteRef(ch, buff, key, ref, reply, done)
            }
        }
        return reply
    }

    /** Collects a pure markdown representation of all citedown
    * content by recursively descending the parse tree.
    * By default, begins from the root node of the parse tree.
    * @returns A markdown String with all CITE URN references resolved to
    * URL values.
    */
    String collectNodes() {
        def root = pr.parseTreeRoot
        return collectNodes(root, pr.inputBuffer, "")
    }

    /** Collects a pure markdown representation of all citedown
    * content by recursively descending the parse tree from node.
    * @param node The node to search from.
    * @param inBuff The underlying parboiled input buffer from which
    * we can extract String values.
    * @param replyStr Accumulated value of the recursively built markdown
    * reply.
    * @returns A markdown String with all CITE URN references resolved to
    * URL values.
    */
    String collectNodes(Object node, Object inBuff, String replyStr ) {

        node.getChildren().each { ch ->
            switch (ch.getLabel()) {

                case "Link":
                // convert CiteLabel and CiteReferenceLink to markdown:
                ArrayList cr = extractCiteRef(ch, inBuff, "", "", [], false) 
                replyStr += "[${cr[0]}]${cr[1]}"
                break

                case "CiteLabel":
                    System.err.println "\tGot a CITE label. " + inBuff.extract(ch.getStartIndex(), ch.getEndIndex())
                break

                case "CiteReferenceLink":
                    System.err.println "\tGot a CITE ref link. " + inBuff.extract(ch.getStartIndex(), ch.getEndIndex())
                break

                case "CiteRefLinkNode":
                    System.err.println "\tGot a CITE ref link NODE " + inBuff.extract(ch.getStartIndex(), ch.getEndIndex())

                break
                
                case "ReferenceLink":
                    System.err.println "Reference link: " + inBuff.extract(ch.getStartIndex(), ch.getEndIndex())
                break
                case "RefSrc":
                    System.err.println "REF SRC " + inBuff.extract(ch.getStartIndex(), ch.getEndIndex())
                break

                case "Reference":
                    if (debug > 0) { System.err.println "Convert reference part to URL: " + inBuff.extract(ch.getStartIndex(), ch.getEndIndex()) }
                // Label and RefSrc
                String refAsUrl = reformatReference(ch, inBuff, "", "", "", false)
                if (debug > 0) { System.err.println "As URL = " + refAsUrl }
                replyStr +=  refAsUrl
                    //replyStr += inBuff.extract(ch.getStartIndex(), ch.getEndIndex())
                break
            
                /* should not get these, maybe*/
                case "Label":
                    System.err.println "REF LABEL : " + inBuff.extract(ch.getStartIndex(), ch.getEndIndex())
                break
                
                case "ReferenceNode":
                    System.err.println "Reference NODE: " + inBuff.extract(ch.getStartIndex(), ch.getEndIndex())
                break

                case "Sp":
                    case "Spacechar":
                    case "NonindentSpace":
                    case "Newline":
                    case "NormalChar":
                    replyStr += inBuff.extract(ch.getStartIndex(), ch.getEndIndex())
                break

                default:
                    replyStr = collectNodes(ch, inBuff, replyStr)
                break
            }
        }
        return replyStr
    }

}


// Possible plugins: perhaps test for compatibility?
//PegDownProcessor pdp = new PegDownProcessor(Extensions.SMARTYPANTS | Extensions.TABLES | Extensions.DEFINITIONS | Extensions.CITE)
