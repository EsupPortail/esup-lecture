projet based on esup-commons V2

Packages generation :
mvn -P portlet2Development clean package
mvn -P portletDevelopment clean package
mvn -P servletDevelopment clean package

Releases management :
mvn release:prepare -DautoVersionSubmodules
mvn -P portletDevelopment release:perform -Dgoals=package

JQuery UI :
- jQuery UI
- UI Dialog
- UI Resizable

Prototyping :
serlet :
 mvn jetty:run
portlet :
 mvn portlet-prototyping:run
 and go on http://localhost:8080/pluto