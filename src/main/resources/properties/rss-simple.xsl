<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method = "html" encoding="utf-8" />
   <xsl:template match="/">
     <h1>
       <xsl:value-of select="/item/title" />
     </h1>
     <xsl:value-of select="/item/description" disable-output-escaping="yes"/>
     <br/>
     <div class="article-block">
     <a target="_blanck" href="{/item/link}">En savoir plus...</a>
     </div>
   </xsl:template>
</xsl:stylesheet>

