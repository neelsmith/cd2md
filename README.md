# cd2md #

A citedown to markdown utility.  Citedown extends markdown with notation for citation using CITE URNs.  The cd2md library uses Ryan Baumann's pegdown parser to parse text formatted in citedown, and convert it to conventional markdown that can be used with any markdown tools.


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

### Embedding Content of URN References (Quoting Sources) ###

Similar to the way conventional markdown identifies an embedded image by preceding a reference to its URL with an exclamation point, citedown uses a preceding exclamation point to indicate that a URN should be quoted rather than cited;  that is, its content should be embedded in the document, rather than linked to.  For example, this URN definition and quotation would embed the content of the first ten lines of the *Iliad* in a processed citedown document:

> `The *Iliad* begins !{with these famous lines}[1].`
> `[1]: urn:cts:greekLit:tlg0012.tlg001:1.1-1.10`






