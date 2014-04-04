<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<div class="lecture-clickable lecture-highlightable fl-list-menu">
			<a target="_blank" href="{item/link}">
				<div class="h4">
					<xsl:value-of select="/item/title" />
				</div>
                <div class="row">
				    <p><xsl:value-of select="/item/description" /></p>
                </div>
			</a>
		</div>
	</xsl:template>
</xsl:stylesheet>

