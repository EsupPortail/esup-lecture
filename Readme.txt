Packages generation :
mvn clean package

Releases management :
mvn release:prepare -DautoVersionSubmodules
mvn -P portletDevelopment release:perform -Dgoals=package

Prototyping :
serlet :
 mvn jetty:run
portlet :
 mvn portlet-prototyping:run
 and go on http://localhost:8080/pluto