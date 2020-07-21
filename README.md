### About

This a sample authenticator that validates username and mobile number to authenticate.
**Note**: here mobile number should be a unique element.

### Try

1. Build the component by executing following command in the root directory
	`$ mvn clean install`

2. Once successfully built, copy org.carbon.custom.userIdentifier-1.0.0.jar (from target) into following directory
    `[IS_HOME]/repository/components/dropins` 

3. Copy the phoneNameVerification.jsp (it is in the root folder) to following directory
    `repository/deployment/server/webapps/authenticationendpoint`

4. Restart the IS
    
5. Open the web.xml file in `/repository/deployment/server/webapps/authenticationendpoint/WEB-INF/web.xml` and add the following
     
         <servlet>
              <servlet-name>customVerifier.do</servlet-name>
              <jsp-file>/phoneNameVerification.jsp</jsp-file>
          </servlet>
          
          <servlet-mapping>
              <servlet-name>customVerifier.do</servlet-name>
              <url-pattern>/customVerifier.do</url-pattern>
          </servlet-mapping>
          
6. Now if you go to a SP edit page Under **Local & Outbound Authentication Configuration**, select **Local authentication** check box.
   You can select this authenticator by choosing **phoneNameVerifierAuthenticator**