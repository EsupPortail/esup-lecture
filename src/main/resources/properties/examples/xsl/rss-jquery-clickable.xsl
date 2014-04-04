<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<div class="lecture-clickable lecture-highlightable">
			<div class="h4">
				<xsl:value-of select="/item/title" />
			</div>
			<div class="lecture-link"  style="display:none">
				Lien :
				<a target="_blank" href="{item/link}">
					<xsl:value-of select="item/link" />
				</a>
				<br />
			</div>
<!-- 			Date de publication : -->
<!-- 			<xsl:value-of select="item/pubDate" /> -->
<!-- 			<br /> -->
<!-- 			Auteur : -->
<!-- 			<xsl:value-of select="item/author" /> -->
<!-- 			<br /> -->
            <div class="row">
                <p>
			        <xsl:value-of select="/item/description" disable-output-escaping="yes" />
                </p>
            </div>
		</div>
	</xsl:template>
</xsl:stylesheet>

