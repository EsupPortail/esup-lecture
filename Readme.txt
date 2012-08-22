projet based on esup-commons V2

Packages generation :
mvn -P portlet2Development clean package
mvn -P portletDevelopment clean package
mvn -P servletDevelopment clean package

Releases management :
don't forget to :
- uncomment cas in web.xml
mvn release:prepare -DautoVersionSubmodules
mvn -P portletDevelopment release:perform -Dgoals=package
mvn -P portlet2Development release:perform -Dgoals=package

JQuery UI :
- jQuery UI
- UI Dialog
- UI Resizable

Prototyping :
serlet :
comment cas in web.xml
mvn jetty:run
portlet :
mvn -P portlet2Development package portlet-prototyping:run
and go on http://localhost:8080/pluto