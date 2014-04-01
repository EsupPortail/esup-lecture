Config de dev (dans src/main/resources/properties/config.properties) :
authenticationService.bean=offlineFixedUserAuthenticationService (force le user à bourges)
externalService.bean=externalServiceStub (reconnait l'utilisateur courant comme membre du groupe local.0)

Packages generation :
mvn clean package

Releases management :
mvn release:prepare -DautoVersionSubmodules
mvn -P portletDevelopment release:perform -Dgoals=package

Prototyping :
portlet :
 mvn portlet-prototyping:run
 and go on http://localhost:8080/pluto