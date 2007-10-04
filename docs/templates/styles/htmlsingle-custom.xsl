<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:xs="http://www.w3.org/2001/XMLSchema"
				version="1.0">

	<xsl:output method="html" encoding="ISO-8859-1" indent="yes"/>

	<!-- ********************************************************************
     $Id: docbookEsup.xsl,v 1.0 10/06/2005 Yohan Colmant $
     ******************************************************************** -->

	<xsl:param name="exist.installation"/>
	<xsl:param name="exist.utilisation"/>
	<xsl:param name="exist.animation"/>
	<xsl:param name="exist.exploitation"/>
	
	<xsl:param name="file.installation"/>
	<xsl:param name="file.utilisation"/>
	<xsl:param name="file.animation"/>
	<xsl:param name="file.exploitation"/>

	<xsl:param name="withindex"/>
	
	<!-- ================== -->
	<!-- == Balises HTML == -->
	<!-- ================== -->

	<!-- HEAD -->
	<xsl:template name="html.head">
		<head>
   			<title><xsl:value-of select="title"/></title>
			<link rel="stylesheet" href="http://www.esup-portail.org/consortium/espace/docbook/esup.css" type="text/css"/>
			<meta name="generator" content="DocBook ESUP XSL Stylesheets V1.0"/>
		</head>
	</xsl:template>

	<!-- BODY -->
	<xsl:template name="html.body">
		<body>
			<div class="pagetitleborder">
				<xsl:apply-templates select="title"/>
				<xsl:apply-templates select="subtitle"/>
			</div>
			
			<xsl:apply-templates select="articleinfo"/>
			
			<xsl:if test="$withindex!='false'">
				<xsl:if test="section!=''">
					<xsl:call-template name="index"/>
				</xsl:if>
			</xsl:if>

			<xsl:apply-templates select="formalpara"/>
			<xsl:apply-templates select="caution"/>
			<xsl:apply-templates select="para"/>

			<!-- section -->
			<xsl:apply-templates select="section">
                <xsl:with-param name="pid" select="0"/>
            </xsl:apply-templates> 
		</body>
	</xsl:template>




	<!-- =========================== -->
	<!-- == Generation de l'index == -->
	<!-- =========================== -->
	
	<xsl:template name="index">
		
			<div class="index">
				<dl>
					<xsl:apply-templates select="section">
               		 	<xsl:with-param name="pid" select="0"/>
						<xsl:with-param name="setTable" select="'true'"/>
        			</xsl:apply-templates>
				</dl>
			</div>

	</xsl:template>
	
	

	<!-- ========================= -->
	<!-- == Texte dans une page == -->
	<!-- ========================= -->
	
	<xsl:template name="text">
		<xsl:apply-templates/>
	</xsl:template>



	<!-- ========================== -->
	<!-- == Balises de "article" == -->
	<!-- ========================== -->

	<!-- Balise "article" -->
	<xsl:template match="/article">
		<html lang="{@lang}">
			<xsl:call-template name="html.head"/>
			<xsl:call-template name="html.body"/>
		</html>
	</xsl:template>
	


	<!-- Balise "title" de "section" -->
	<xsl:template match="title">
		<xsl:param name="pid"/>
		<xsl:variable name="parentnode" select="name(..)"/>

		<!-- If the parent node is "article" -->
		<xsl:if test="$parentnode='article'">
			<span class="pagetitle">
				<xsl:value-of select="."/>
			</span>
			<br/>
		</xsl:if>

		<!-- If the parent node is "section" -->
		<xsl:if test="$parentnode='section'">
			<xsl:variable name="markuplevel" select="count(ancestor::section)"/>
			<xsl:choose>
				<xsl:when test="$markuplevel='1'">
					<h1>
						<xsl:value-of select="$pid"/>.  <xsl:value-of select="."/>
					</h1>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$markuplevel='2'">
							<h2>
								<xsl:value-of select="$pid"/>.  <xsl:value-of select="."/>
							</h2>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="$markuplevel='3'">
									<h3>
										<xsl:value-of select="$pid"/>.  <xsl:value-of select="."/>
									</h3>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="$markuplevel='4'">
											<h4>
												<xsl:value-of select="$pid"/>.  <xsl:value-of select="."/>
											</h4>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose>
												<xsl:when test="$markuplevel='5'">
													<h5>
														<xsl:value-of select="$pid"/>.  <xsl:value-of select="."/>
													</h5>
												</xsl:when>
												<xsl:otherwise>
													<xsl:choose>
														<xsl:when test="$markuplevel='6'">
															<h6>
																<xsl:value-of select="$pid"/>.  <xsl:value-of select="."/>
															</h6>
														</xsl:when>
														<xsl:otherwise>
															<p class="hx">
																<xsl:value-of select="."/>
															</p>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		
		<!-- If the parent node is "formalpara" -->
		<xsl:if test="$parentnode='formalpara'">
			<span class="formalparatitle">
				<xsl:value-of select="."/>
			</span>
		</xsl:if>


		<!-- If the parent node is "step" -->
		<xsl:if test="$parentnode='step'">
			<p class="steptitle">
				<xsl:value-of select="."/>
			</p>
		</xsl:if>

		<!-- If the parent node is "step" -->
		<xsl:if test="$parentnode='objectinfo'">
			<p class="objectinfotitle">
				<xsl:value-of select="."/>
			</p>
		</xsl:if>

		<!-- If the parent node is "table" -->
		<xsl:if test="$parentnode='table'">
			<p class="tabletitle">
				<xsl:value-of select="."/>
			</p>
		</xsl:if>
		

	</xsl:template>

	<!-- Balise "subtitle" -->
	<xsl:template match="subtitle">
		<span class="pagesubtitle">
			<xsl:value-of select="."/>
		</span>
		<br/>
	</xsl:template>

	<!-- Balise "articleinfo" -->
	<xsl:template match="articleinfo">
		<div class="articleinfo">
		<table>
			<tr>
				<td>
					<xsl:if test="mediaobject/imageobject/imagedata/@fileref!=''">
						<img src="{mediaobject/imageobject/imagedata/@fileref}" align="left"/>	
					</xsl:if>
				</td>
				<td valign="center" width="100%">
					<xsl:apply-templates select="abstract"/>		
					<xsl:apply-templates select="releaseinfo"/>
			
					<xsl:if test="$exist.installation='true'">
						<a class="otherpages" href="{$file.installation}">Installation</a><br/>
					</xsl:if>
					<xsl:if test="$exist.utilisation='true'">
						<a class="otherpages" href="{$file.utilisation}">Utilisation</a><br/>
					</xsl:if>
					<xsl:if test="$exist.animation='true'">
						<a class="otherpages" href="{$file.animation}">Animation</a><br/>
					</xsl:if>
					<xsl:if test="$exist.exploitation='true'">
						<a class="otherpages" href="{$file.exploitation}">Exploitation</a><br/>
					</xsl:if>
				</td>
			</tr>
		</table>
		
		<xsl:apply-templates select="author"/>
		<xsl:apply-templates select="orgname"/>
		
		<xsl:if test="copyright!=''">
			<br/>
		</xsl:if>
		<xsl:apply-templates select="copyright"/>
		<xsl:apply-templates select="revhistory"/>
		
		</div>
	</xsl:template>
	


	<!-- Balise "formalpara" -->
	<xsl:template match="formalpara">
		<dl>
			<dt>
				<xsl:apply-templates select="title"/>
			</dt>
			<dd>
				<xsl:apply-templates select="para"/>
			</dd>
		</dl>
	</xsl:template>
	
	<!-- Balise "caution" -->
	<xsl:template match="caution">
		<div class="caution">
			<p class="cautiontitle">
				<xsl:choose>
					<xsl:when test="title!=''">
						<xsl:value-of select="title"/>
					</xsl:when>
					<xsl:otherwise>
						Attention
					</xsl:otherwise>
				</xsl:choose>
			</p>
			<xsl:apply-templates select="para"/>
		</div>
	</xsl:template>
	
	<!-- Balise "section" -->
	<xsl:template name="section" match="section">
		<xsl:param name="pid"/>
        <xsl:param name="setTable"/>
		
		<!-- The id of this section -->
		<xsl:variable name="ids">
        	<xsl:choose>
				<xsl:when test="$pid='0'">
        			<xsl:value-of select="count(./preceding-sibling::section) + 1"/>
				</xsl:when>

				<xsl:otherwise>
					<xsl:value-of select="concat($pid,'.',(count(./preceding-sibling::section) + 1))"/>
				</xsl:otherwise>
			</xsl:choose>
        </xsl:variable>
		
		
		<xsl:choose>

			<!-- set the table -->
			<xsl:when test="$setTable='true'">
				<dt>
					<span class="sectionmenu"><a href="#{$ids}"><xsl:value-of select="$ids"/>.  <xsl:value-of select="title"/></a></span>
				</dt>
				<xsl:if test="section!=''">
					<xsl:if test="(count(./ancestor::section)) &lt; 2">
						<dd>
							<dl>
								<xsl:apply-templates select="section">
               	 					<xsl:with-param name="pid" select="$ids"/>
									<xsl:with-param name="setTable" select="$setTable"/>
        						</xsl:apply-templates>
							</dl>
						</dd>
					</xsl:if>
				</xsl:if>
			</xsl:when>

			<!-- don't set the table, show the section -->
			<xsl:otherwise>
				<a name="{$ids}"></a>
				<xsl:if test="@id!=''">
					<a name="{@id}"></a>
				</xsl:if>		
				<xsl:apply-templates>
               	 	<xsl:with-param name="pid" select="$ids"/>
        		</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
				
		
	</xsl:template>


	


	<!-- ============================== -->
	<!-- == Balises de "articleinfo" == -->
	<!-- ============================== -->

	<!-- Balise "copyright" -->
	<xsl:template match="copyright">
		<span class="copyright">
			Copyright ©
			&#160;<xsl:apply-templates select="year"/>
			&#160;<xsl:apply-templates select="holder"/>
		</span>
		<br/>
	</xsl:template>
	
	<!-- Balise "orgname" -->
	<xsl:template match="orgname">
		<span class="orgname">
			<xsl:apply-templates>
				<xsl:with-param name="top" select="true"/>
			</xsl:apply-templates>
		</span>
	</xsl:template>

	<!-- Balise "releaseinfo" -->
	<xsl:template match="releaseinfo">
		<div class="releaseinfo">
			<xsl:apply-templates/>
		</div>
		<br/>
	</xsl:template>

	<!-- Balise "abstract" -->
	<xsl:template match="abstract">
		<div class="abstract">
			<xsl:apply-templates/>
		</div>
		<br/>
	</xsl:template>

	<!-- Balise "author" -->
	<xsl:template match="author">
		<xsl:apply-templates select="firstname"/>&#160;
		<xsl:apply-templates select="surname"/>&#160;
		<br/>
		<xsl:apply-templates select="affiliation"/>
		<br/>
	</xsl:template>

	<!-- Balise "revhistory" -->
	<xsl:template match="revhistory">
		<div class="maj">
			<table summary="Revision history" border="0" width="100%">
				<tbody>
					<tr>
  						<th colspan="3" class="majtitle" align="left" valign="top">Dates de modification</th>
					</tr>
  					<xsl:apply-templates/>
				</tbody>
			</table>
		</div>
	</xsl:template>

	
	<!-- ============================== -->
	<!-- == Balises de "copyright" == -->
	<!-- ============================== -->

	<!-- Balise "year" -->
	<xsl:template match="year">
		<xsl:apply-templates/>
	</xsl:template>
	
	<!-- Balise "holder" -->
	<xsl:template match="holder">
		<xsl:apply-templates/>
	</xsl:template>



	<!-- ============================== -->
	<!-- == Balises de "orgname" == -->
	<!-- ============================== -->

	<!-- Balise "inlinemediaobject" -->
	<xsl:template match="inlinemediaobject">
		<xsl:apply-templates/>
	</xsl:template>
	


	<!-- ============================== -->
	<!-- == Balises de "releaseinfo" == -->
	<!-- ============================== -->

	<!-- Balise "remark" -->
	<xsl:template match="remark">
		<span class="remark">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>



	<!-- ========================= -->
	<!-- == Balises de "author" == -->
	<!-- ========================= -->

	<!-- Balise "firstname" -->
	<xsl:template match="firstname">
		<span class="firstname">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>
	
	<!-- Balise "surname" -->
	<xsl:template match="surname">
		<span class="surname">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>
	
	<!-- Balise "affiliation" -->
	<xsl:template match="affiliation">
		<div class="affiliation">
			<xsl:apply-templates select="orgname"/>
		</div>
	</xsl:template>





	<!-- ========================== -->
	<!-- == Balises de "revhistory" == -->
	<!-- ========================== -->

	<!-- Balise "revision" -->
	<xsl:template match="revision">
		<tr>
			<td class="minuscule" align="left"><nobr>Revision&#160;<xsl:value-of select="revnumber"/></nobr></td>
			<td class="minuscule" align="left"><nobr><xsl:value-of select="date"/></nobr></td>
			<td class="minuscule" align="left"><nobr><xsl:value-of select="revdescription/para"/></nobr></td>
		</tr>
	</xsl:template>
	







	<!-- ========================== -->
	<!-- == Balises de "section" == -->
	<!-- ========================== -->

	<!-- Balise "itemizedlist" -->
	<xsl:template match="itemizedlist">
		<div>
			<ul>
				<xsl:for-each select="listitem">
					<li>
						<xsl:apply-templates/>
					</li>
				</xsl:for-each>
			</ul>
		</div>
	</xsl:template>

	<!-- Balise "variablelist" -->
	<xsl:template match="variablelist">
		<div>
			<dl>
				<xsl:apply-templates/>
			</dl>
		</div>
	</xsl:template>

	<!-- Balise "varlistentry" -->
	<xsl:template match="varlistentry">
		<dt>
			<span class="term"><xsl:value-of select="term"/></span>
		</dt>
		<dd>
			<xsl:apply-templates select="listitem"/>
		</dd>
	</xsl:template>
	
	<!-- Balise "para" -->
	<xsl:template match="para">
		<p>
			<xsl:call-template name="text"/>
		</p>
	</xsl:template>

	<!-- Balise "procedure" -->
	<xsl:template match="procedure">
		<ul>
		<xsl:apply-templates/>
		</ul>
	</xsl:template>

	<!-- Balise "screen" -->
	<xsl:template match="screen">
		<pre class="screen">
			<xsl:value-of select="."/>
		</pre>
	</xsl:template>

	

	<!-- ============================ -->
	<!-- == Balises de "procedure" == -->
	<!-- ============================ -->

	<!-- Balise "step" -->
	<xsl:template match="step">
		<li>
			<xsl:apply-templates/>
		</li>
	</xsl:template>

	

	<!-- ============================ -->
	<!-- == Balises de "step" == -->
	<!-- ============================ -->

	<!-- Balise "substeps" -->
	<xsl:template match="substeps">
		<ul>
			<xsl:apply-templates/>
		</ul>
	</xsl:template>




	<!-- ======================= -->
	<!-- == Balises de "para" == -->
	<!-- ======================= -->

	<!-- Balise "important" -->
	<xsl:template match="important">
		<div class="important">
			<p class="importanttitle">
				<xsl:choose>
					<xsl:when test="title!=''">
						<xsl:value-of select="title"/>
					</xsl:when>
					<xsl:otherwise>
						Important
					</xsl:otherwise>
				</xsl:choose>
			</p>
			<xsl:apply-templates select="para"/>
		</div>
	</xsl:template>
	
	<!-- Balise "note" -->
	<xsl:template match="note">
		<div class="note">
			<p class="notetitle">
				<xsl:choose>
					<xsl:when test="title!=''">
						<xsl:value-of select="title"/>
					</xsl:when>
					<xsl:otherwise>
						Note
					</xsl:otherwise>
				</xsl:choose>
			</p>
			<xsl:apply-templates select="para"/>
		</div>
	</xsl:template>

	<!-- Balise "ulink" -->
	<xsl:template match="ulink">
		<xsl:param name="top"/>
		
		<xsl:choose>
			<xsl:when test="$top='true'">
				<a href="{@url}" target="_top"><xsl:value-of select="."/></a>
			</xsl:when>
			<xsl:otherwise>
				<a href="{@url}"><xsl:value-of select="."/></a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Balise "xref" -->
	<xsl:template match="xref">
		<a href="#{@linkend}">
			<xsl:variable name="sectionId" select="@linkend"/>
			Section&#160;"
			<xsl:value-of select="//section[@id=$sectionId]/title"/>"
		</a>
	</xsl:template>

	

	<!-- Balise "programlisting" -->
	<xsl:template match="programlisting">
		<pre class="programlisting">
			<xsl:call-template name="text"/>
		</pre>
	</xsl:template>

	<!-- Balise "emphasis" -->
	<xsl:template match="emphasis">
		<xsl:choose>
			<xsl:when test="@role='bold'">
				<span class="emphasisbold">
					<xsl:value-of select="."/>
				</span>
			</xsl:when>
			<xsl:otherwise>
				<span class="emphasis">
					<xsl:value-of select="."/>
				</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Balise "literal" -->
	<xsl:template match="literal">
		<span class="literal">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>

	<!-- Balise "mediaobject" -->
	<xsl:template match="mediaobject">
		<div class="mediaobject">
			<xsl:apply-templates/>
		</div>
	</xsl:template>

	<!-- Balise "table" -->
	<xsl:template match="table">
		<xsl:apply-templates select="title"/>
		<xsl:choose>
			<xsl:when test="title!=''">
				<xsl:choose>
					<xsl:when test="tgroup/@align!=''">
						<table summary="Titre de la table" border="1" align="{tgroup/@align}">
							<xsl:apply-templates select="tgroup"/>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<table summary="Titre de la table" border="1">
							<xsl:apply-templates select="tgroup"/>
						</table>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="tgroup/@align!=''">
						<table border="1" align="{tgroup/@align}">
							<xsl:apply-templates select="tgroup"/>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<table border="1">
							<xsl:apply-templates select="tgroup"/>
						</table>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Balise "command" -->
	<xsl:template match="command">
		<span class="command">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>

	<!-- Balise "varname" -->
	<xsl:template match="varname">
		<span class="varname">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>

	<!-- Balise "filename" -->
	<xsl:template match="filename">
		<span class="filename">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>

	<!-- Balise "acronym" -->
	<xsl:template match="acronym">
		<span class="acronym">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>

	<!-- Balise "userinput" -->
	<xsl:template match="userinput">
		<span class="userinput">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>




	<!-- ============================== -->
	<!-- == Balises de "mediaobject" == -->
	<!-- ============================== -->

	<!-- Balise "objectinfo" -->
	<xsl:template match="objectinfo">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Balise "imageobject" -->
	<xsl:template match="imageobject">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Balise "audioobject" -->
	<xsl:template match="audioobject">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Balise "videoobject" -->
	<xsl:template match="videoobject">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Balise "caption" -->
	<xsl:template match="caption">
		<p class="caption">
			<xsl:value-of select="."/>
		</p>
	</xsl:template>

	<!-- Balise "imagedata" -->
	<xsl:template match="imagedata">
		<xsl:choose>
			<xsl:when test="@align!=''">
				<img src="{@fileref}" align="{@align}"/>
			</xsl:when>
			<xsl:otherwise>
				<img src="{@fileref}"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Balise "audiodata" -->
	<xsl:template match="audiodata">
		<a href="{@fileref}"> Ecouter </a>
	</xsl:template>

	<!-- Balise "videodata" -->
	<xsl:template match="videodata">
		<a href="{@fileref}"> Voir la vidéo </a>
	</xsl:template>




	<!-- ======================== -->
	<!-- == Balises de "table" == -->
	<!-- ======================== -->

	<!-- Balise "tgroup" -->
	<xsl:template match="tgroup">
		<xsl:variable name="percent" select="(100-(100 mod @cols)) div @cols"/>
				
		<colgroup>
			<xsl:call-template name="col">
				<xsl:with-param name="cols"><xsl:value-of select="@cols"/></xsl:with-param>
				<xsl:with-param name="percent"><xsl:value-of select="$percent"/></xsl:with-param>
				<xsl:with-param name="index"><xsl:value-of select="@cols"/></xsl:with-param>
   			</xsl:call-template>
		</colgroup>

		<xsl:apply-templates/>
	</xsl:template>
	
	<!-- Recurrence de col -->
	<xsl:template name="col">
		<xsl:param name="cols"/>
		<xsl:param name="percent"/>
		<xsl:param name="index"/>
		
		<xsl:choose>
			<xsl:when test="$index=1">
				<xsl:variable name="lastpercent" select="100 - ($percent*($cols - 1))"/>
				<col width="{$lastpercent}%"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="$index > 0">
					<col width="{$percent}%"/>
					<xsl:call-template name="col">
    					<xsl:with-param name="cols"><xsl:value-of select="$cols"/></xsl:with-param>
						<xsl:with-param name="percent"><xsl:value-of select="$percent"/></xsl:with-param>
						<xsl:with-param name="index" select="$index - 1"/>
   					</xsl:call-template>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>


	<!-- Balise "tbody" -->
	<xsl:template match="tbody">
		<tbody>
			<xsl:apply-templates/>
		</tbody>
	</xsl:template>

	<!-- Balise "row" -->
	<xsl:template match="row">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>

	<!-- Balise "entry" -->
	<xsl:template match="entry">
		<td align="{@align}" valign="{@valign}">
			<xsl:apply-templates/>
		</td>
	</xsl:template>






	<!-- ==================================================================== -->
</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2005. Progress Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\pages\CSignetCerimes\utilisation.xml" htmlbaseurl="" outputurl="" processortype="internal" useresolver="no" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal" customvalidator=""/></scenarios><MapperMetaTag><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no" ><SourceSchema srcSchemaPath="..\pages\CSignetCerimes\utilisation.xml" srcSchemaRoot="article" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/></MapperInfo><MapperBlockPosition><template name="html.head"></template><template name="index"></template><template match="xref"></template></MapperBlockPosition><TemplateContext></TemplateContext></MapperMetaTag>
</metaInformation>
-->