package org.carbon.custom;

/**
 * Constant class for phoneNameVerifierAuthenticator.
 */
public class phoneNameVerifierAuthenticatorConstants {

    public static final String AUTHENTICATOR_NAME = "phoneNameVerifier";
    public static final String AUTHENTICATOR_FRIENDLY_NAME = "phoneNameVerifierAuthenticator";
    public static final String INPUT_NAME = "userName";
    public static final String INPUT_MOBILE = "mobile";
    public static final int SUPER_TENANT_ID = -1234;
    public static final String CLAIM_MOBILE = "http://wso2.org/claims/mobile";
    public static final String CUSTOM_LOGIN_PAGE = "authenticationendpoint/customVerifier.do";
    public static final String DEFAULT_LOGIN_PAGE = "authenticationendpoint/login.do";
    public static final String SESSION_DATA_KEY = "sessionDataKey";
}
