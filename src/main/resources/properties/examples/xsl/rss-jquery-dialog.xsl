<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<div>
			<!-- ui-dialog -->
			<div class="dialog" id="{/item/link}">
				<iframe frameborder="0" hspace="0" style="width:820px;height:600px;">
					<xsl:attribute name="src">
						<xsl:value-of select="item/link" />
					</xsl:attribute>
					TEST
				</iframe>
			</div>
			<h1>
				<xsl:value-of select="/item/title" />
			</h1>
			<xsl:value-of select="/item/description"
				disable-output-escaping="yes" />
			<br />
			<div>
				<a href="{/item/link}" class="ui-state-default ui-corner-all dialog_link">En savoir plus ...</a>
			</div>
		</div>
	</xsl:template>
</xsl:stylesheet>

