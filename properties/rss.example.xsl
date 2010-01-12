<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:b="http://purl.org/rss/1.0/" 
xmlns:content="http://purl.org/rss/1.0/modules/content/" >
<xsl:output method = "html" encoding="utf-8" />
   <xsl:template match="/">
     <h1>
       <xsl:value-of select="/b:item/b:title" />
     </h1>
     <br/>
     <xsl:value-of select="/b:item/b:description" disable-output-escaping="yes"/>
     <br/>
     <a target="_blank" href="{/b:item/b:link}">En savoir plus...</a>
   </xsl:template>
</xsl:stylesheet>

