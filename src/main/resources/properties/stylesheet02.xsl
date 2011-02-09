<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method = "html" encoding="utf-8" />
   <xsl:template match="/">
   <h1>
      <xsl:value-of select="/item/title" />
   </h1>

   Lien : <a target="_blank" href="{item/link}"><xsl:value-of select="item/link" /></a><br/>
   Date de publication : <xsl:value-of select="item/pubDate" /><br/>
   Auteur : <xsl:value-of select="item/author" /><br/>
   <xsl:value-of select="/item/description" disable-output-escaping="yes"/>
   </xsl:template>
</xsl:stylesheet>

