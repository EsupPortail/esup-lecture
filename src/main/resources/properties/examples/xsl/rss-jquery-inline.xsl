<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dc="http://purl.org/dc/elements/1.1/">
	<xsl:output method="html" encoding="utf-8"/>
	<xsl:template match="/">
		<div class="h4">
			<xsl:value-of select="/item/title"/>
		</div>
		
		<xsl:variable name="date_en" select="/item/pubDate"/>
		<xsl:variable name="jour_en" select="substring-before($date_en,',')"/>
		<xsl:variable name="jour_num" select="substring($date_en,6,2)"/>
		<xsl:variable name="mois_en" select="substring($date_en,9,3)"/>
		<xsl:variable name="annee" select="substring($date_en,13,4)"/>
		<xsl:variable name="heure" select="substring($date_en,18,5)"/>
   
   		<div class="row">
			<div class="metaArticleLogo"/>
			<p>
                <xsl:if test="(/item/dc:creator) and /item/dc:creator != '' ">
                    <div> Auteur : <xsl:value-of select="/item/dc:creator"/></div>
                </xsl:if>
                <xsl:if test="(/item/dc:creator) and /item/dc:creator != '' ">
                    <div> Auteur : <xsl:value-of select="/item/dc:creator"/></div>
                </xsl:if>
                <xsl:if test="$date_en !=''">
				<span>
				  Date de publication :
				   <xsl:choose>
				   <xsl:when test="$jour_en ='Mon'">Lundi </xsl:when>
				   <xsl:when test="$jour_en ='Tue'">Mardi </xsl:when>
				   <xsl:when test="$jour_en ='Wed'">Mercredi </xsl:when>
				   <xsl:when test="$jour_en ='Thu'">Jeudi </xsl:when>
				   <xsl:when test="$jour_en ='Fri'">Vendredi </xsl:when>
				   <xsl:when test="$jour_en ='Sat'">Samedi </xsl:when>
				   <xsl:when test="$jour_en ='Sun'">Dimanche </xsl:when>
				   </xsl:choose>
				   <xsl:value-of select="$jour_num"/>
				   <xsl:choose>
				   <xsl:when test="$mois_en ='Jan'"> Janvier </xsl:when>
				   <xsl:when test="$mois_en ='Feb'"> Février </xsl:when>
				   <xsl:when test="$mois_en ='Mar'"> Mars </xsl:when>
				   <xsl:when test="$mois_en ='Apr'"> Avril </xsl:when>
				   <xsl:when test="$mois_en ='May'"> Mai </xsl:when>
				   <xsl:when test="$mois_en ='Jun'"> Juin </xsl:when>
				   <xsl:when test="$mois_en ='Jul'"> Juillet </xsl:when>
				   <xsl:when test="$mois_en ='Aug'"> Août </xsl:when>
				   <xsl:when test="$mois_en ='Sep'"> Septembre </xsl:when>
				   <xsl:when test="$mois_en ='Oct'"> Octobre </xsl:when>
				   <xsl:when test="$mois_en ='Nov'"> Novembre </xsl:when>
				   <xsl:when test="$mois_en ='Dec'"> Décembre </xsl:when>
				   </xsl:choose>
				   <xsl:value-of select="$annee"/>
				   à
				   <xsl:value-of select="$heure"/> GMT
				</span>
                </xsl:if>
			</p>
		</div>
		
		<div class="row">
            <div class="replierArticle" style="display:none">
                <a href="#">Replier...</a>
            </div>
            <p>
                <xsl:value-of select="/item/description" disable-output-escaping="yes"/>
                <br/>
            </p>
            <div>
                <a class="ui-state-default ui-corner-all dialog_link" target="_blank" href="{/item/link}" title="{/item/title}">En savoir plus...</a>
            </div>
		</div>
	</xsl:template>
</xsl:stylesheet>