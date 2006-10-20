<%@include file="_include.jsp"%>
<h:form id="navigationForm">
<%/*
	<e:paragraph value="sessionController = {0}">
		<f:param value="#{sessionController.debug}"/>
	</e:paragraph>
*/%>
	<e:menu>
		<e:menuItem id="welcome" value="#{msgs['NAVIGATION.TEXT.WELCOME']}"
			action="navigationWelcome"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.WELCOME']}" />
		<e:menuItem id="things" action="navigationThings"
			value="#{msgs['NAVIGATION.TEXT.THINGS']}"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.THINGS']}"
			rendered="#{thingsController.pageAuthorized}" />
		<e:menuItem id="files" action="navigationFiles"
			value="#{msgs['NAVIGATION.TEXT.FILES']}"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.FILES']}"
			rendered="#{filesController.pageAuthorized}" />
		<e:menuItem id="departments"
			value="#{msgs['NAVIGATION.TEXT.DEPARTMENTS']}"
			action="navigationDepartments"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.DEPARTMENTS']}"
			rendered="#{departmentsController.pageAuthorized}" />
		<e:menuItem id="administrators"
			value="#{msgs['NAVIGATION.TEXT.ADMINISTRATION']}"
			action="navigationAdministrators"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.ADMINISTRATION']}"
			rendered="#{administratorsController.pageAuthorized}" />
		<e:menuItem id="preferences"
			value="#{msgs['NAVIGATION.TEXT.PREFERENCES']}"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.PREFERENCES']}"
			action="navigationPreferences" 
			rendered="#{preferencesController.pageAuthorized}" />
		<e:menuItem id="about" value="#{msgs['NAVIGATION.TEXT.ABOUT']}"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.ABOUT']}"
			action="navigationAbout" />
		<e:menuItem id="login" action="navigationLogin"
			value="#{msgs['NAVIGATION.TEXT.LOGIN']}"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.LOGIN']}"
			rendered="#{sessionController.printLogin}" />
		<e:menuItem id="logout" action="#{sessionController.logout}"
			value="#{msgs['NAVIGATION.TEXT.LOGOUT']}"
			accesskey="#{msgs['NAVIGATION.ACCESSKEY.LOGOUT']}"
			rendered="#{sessionController.printLogout}" />
	</e:menu>
</h:form>
