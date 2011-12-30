<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<div>
			<h1>
				<xsl:value-of select="/item/title" />
			</h1>
			<div>
				Lien :
				<a target="_blank" class="thickbox">
					<xsl:attribute name="href">
    				    <xsl:value-of select="item/link" />?KeepThis=true&amp;TB_iframe=true&amp;height=555&amp;width=830</xsl:attribute>	
					En savoir plus ...
				</a>
				<br />
			</div>
			Date de publication :
			<xsl:value-of select="item/pubDate" />
			<br />
			Auteur :
			<xsl:value-of select="item/author" />
			<br />
			<xsl:value-of select="/item/description"
				disable-output-escaping="yes" />
		</div>
	</xsl:template>
</xsl:stylesheet>

