<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<div class="lecture-clikable lecture-highlightable">
			<h1>
				<xsl:value-of select="/item/title" /> !MOBILE!
			</h1>
			<div class="lecture-link">
				Lien :
				<a target="_blank" href="{item/link}">
					<xsl:value-of select="item/link" />
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

