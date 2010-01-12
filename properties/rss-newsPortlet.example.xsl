<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method = "html" encoding="utf-8" />
  <xsl:template match="/">
    <h1>
      <xsl:value-of select="/item/title" />
    </h1>
    <xsl:value-of select="/item/description" disable-output-escaping="yes"/>
    <br/>&#160;
    <a target="_blank" class="thickbox">
     <xsl:attribute name="id">
      <xsl:value-of select="concat('lienClassique', substring-after(/item/link, 'itemID='))" />
     </xsl:attribute>
     <xsl:attribute name="href">
      <xsl:call-template name="replace-string">
          <xsl:with-param name="text" select="/item/link"/>
          <xsl:with-param name="replace" select="'localhost:8854'"/>
          <xsl:with-param name="with" select="'ent.univ-rennes1.fr'"/>
      </xsl:call-template>&amp;KeepThis=true&amp;TB_iframe=true&amp;height=555&amp;width=830</xsl:attribute>
     En savoir plus ...
    </a>
  </xsl:template>


 <xsl:template name="replace-string">
   <xsl:param name="text"/>
   <xsl:param name="replace"/>
   <xsl:param name="with"/>
   <xsl:choose>
     <xsl:when test="contains($text,$replace)">
       <xsl:value-of select="substring-before($text,$replace)"/>
       <xsl:value-of select="$with"/>
       <xsl:call-template name="replace-string">
         <xsl:with-param name="text" select="substring-after($text,$replace)"/>
         <xsl:with-param name="replace" select="$replace"/>
         <xsl:with-param name="with" select="$with"/>
       </xsl:call-template>
     </xsl:when>
     <xsl:otherwise>
       <xsl:value-of select="$text"/>
     </xsl:otherwise>
   </xsl:choose>
 </xsl:template>

</xsl:stylesheet>
