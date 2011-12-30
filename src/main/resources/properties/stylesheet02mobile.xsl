<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<div class="lecture-clikable lecture-highlightable fl-list-menu">
			<a target="_blank" href="{item/link}">
				<h3>
					<xsl:value-of select="/item/title" />
				</h3>
				<p><xsl:value-of select="/item/description" /></p>
			</a>
		</div>
	</xsl:template>
</xsl:stylesheet>

