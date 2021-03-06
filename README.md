# cd2md #

A citedown to markdown utility.  Citedown extends markdown with notation for citation using CITE URNs.  The cd2md library uses Ryan Baumann's pegdown parser to parse text formatted in citedown, and convert it to conventional markdown that can be used with any markdown tools.

Ryan's parser converts citedown to HTML expecting Chris Blackwell's CITEKit to resolve URNs, and deferring resolution of all URNs to CITEKit.  CITEKit supports complex configuration of sources and allows configuration of multiple sources.  The result is HTML with javascript dynamically resolving URNs and linking or retrieving content.

This library converts citedown to pure markdown.  It directly resolves URNs.  It allows only a single source for each CITE type. The result is pure markdown that can be used with any markdown tool.



## Overview of citedown ##

Markdown includes a notation for URLs, but has no way to identify scholarly references to technology-agnostic URNs.  Citedown identifies URNs with the following conventions.

### Defining URNs ###

Citable URNs are defined with the same reference syntax as URLs.  For example

> `[1]: urn:cite:hmt:msA.12r`

defines a URN identifying a page of the Venetus A manuscript as reference 1.

### Linking to URN References (citing sources)###

A span of markdown source text can be linked to a reference by marking it with curly brackets (braces), followed by the reference identifier in square brackets.  For example, in the markdown source text

> `In the Venetus A manuscript, the *Iliad* text begins on folio {twelve recto}[1].`

the string `twelve recto` is linked to the URN identified by reference `1`.

### Embedding Content of URN References (quoting sources) ###

Similar to the way conventional markdown identifies an embedded image by preceding a reference to its URL with an exclamation point, citedown uses a preceding exclamation point to indicate that a URN should be quoted rather than cited;  that is, its content should be embedded in the document, rather than linked to.  For example, this URN definition and quotation would embed the content of the first ten lines of the *Iliad* in a processed citedown document:

> `The *Iliad* begins !{insert famous lines here}[1].`
>
> `[1]: urn:cts:greekLit:tlg0012.tlg001:1.1-1.10`


## Using the code library##

By default, `gradle assemble` builds a "fat jar" with all dependencies included, so if you have the single
`cd2md` jar on your CLASSPATH, you can 
construct a CitedownConverter and change default values for CITE services like this:

    CitedownConverter c2m = new CitedownConverter()
    c2m.cts = "http://www.homermultitext.org/hmt-digital/texts"

The `toMarkdown` method takes a String of citedown text, and returns pure markdown, e.g.,


    String md = c2m.toMarkdown(citedownSource)
    
## State of implementation and known bugs ##

Standard markdown currently implemented:

- atx-style headers ("#" headers)
- emphasis and strong emphasis using asterisks
- unordered lists using hyphens
- ordered lists
 
Citedown extensions:

- URNs in reference definitions are resolved to URLs
- citedown links with curly brackets implemented for CTS texts, CITE Objects, and CITE Images
- citedown quotes with "!" and curly brackets implemented for CITE Images


Known bugs:

- links within list items are not correctly formatted



