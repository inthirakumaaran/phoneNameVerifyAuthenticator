package org.carbon.custom;

import org.apache.commons.lang.StringUtils;
import org.carbon.custom.internal.phoneNameVerifierAuthenticatorDataHolder;
import org.wso2.carbon.identity.application.authentication.framework.AbstractApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.LocalApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.core.model.IdentityErrorMsgContext;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.carbon.custom.phoneNameVerifierAuthenticatorConstants.CLAIM_MOBILE;
import static org.carbon.custom.phoneNameVerifierAuthenticatorConstants.DEFAULT_LOGIN_PAGE;
import static org.carbon.custom.phoneNameVerifierAuthenticatorConstants.INPUT_MOBILE;
import static org.carbon.custom.phoneNameVerifierAuthenticatorConstants.INPUT_NAME;
import static org.carbon.custom.phoneNameVerifierAuthenticatorConstants.SESSION_DATA_KEY;
import static org.carbon.custom.phoneNameVerifierAuthenticatorConstants.SUPER_TENANT_ID;
import static org.wso2.carbon.user.core.UserCoreConstants.DEFAULT_PROFILE;

/**
 * Authenticator of phoneNameVerifier.
 */
public class phoneNameVerifierAuthenticator extends AbstractApplicationAuthenticator
        implements LocalApplicationAuthenticator {

    private static Log log = LogFactory.getLog(phoneNameVerifierAuthenticator.class);

    /**
     * This method is used to initiate the authentication request
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param context AuthenticationContext
     * @throws AuthenticationFailedException
     */
    @Override
    protected void initiateAuthenticationRequest(HttpServletRequest request,
                                                 HttpServletResponse response, AuthenticationContext context)
        throws AuthenticationFailedException {

        // Use this method to initiate the authentication request. Eg. this may be used to redirect user to a page
        // for submitting credentials.

        String sessionDataKeyParam = getSessionDataKey(context);

        try {
            String retryParam;
            String phoneUsernamePage = DEFAULT_LOGIN_PAGE;
            String queryParams = "?" + sessionDataKeyParam;
            phoneUsernamePage = phoneUsernamePage + queryParams;

            if (context.isRetrying()) {
                retryParam = "&authFailure=true&authFailureMsg=login.fail.message";
                response.sendRedirect(phoneUsernamePage + retryParam);
            } else {
                response.sendRedirect(phoneUsernamePage);
            }

        } catch (IOException e) {
            throw new AuthenticationFailedException("Failed to show the user login page.", e);
        }
    }

    /**
     * This method is used to handle the authentication request, this would be a check to see if request has both
     * username and password params
     *
     * @param request HttpServletRequest
     */
    public boolean canHandle(HttpServletRequest request) {

        // In a multi option scenario, check whether the request from user is meant to this authenticator or not.
        // eg. For basic authentication, this would be a check to see if request has both username and password params.
        return StringUtils.isNotBlank(request.getParameter(INPUT_NAME)) &&
                StringUtils.isNotBlank(request.getParameter(INPUT_MOBILE));
    }

    /**
     * This method is used to process the authentication response
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param context AuthenticationContext
     * @throws AuthenticationFailedException
     */
    @Override
    protected void processAuthenticationResponse(HttpServletRequest request,
                                                 HttpServletResponse response, AuthenticationContext context)
        throws AuthenticationFailedException {

        // Does the actual authentication

        try {
            UserRealm userRealm = phoneNameVerifierAuthenticatorDataHolder.getInstance().getRealmService()
                    .getTenantUserRealm(SUPER_TENANT_ID);
            String userName = validateMobileNumber(request, userRealm);
            if (StringUtils.isBlank(userName)) {
                throw new AuthenticationFailedException("Entered username and phoneNumber doesn't not match");
            }
            context.setSubject(AuthenticatedUser.createLocalAuthenticatedUserFromSubjectIdentifier(userName));
        } catch (UserStoreException e) {
            throw new AuthenticationFailedException("Error while getting user store manager.", e);
        }
    }

    /**
     * This method is to get the correlation ID from the request
     *
     * @param request HttpServletRequest
     */
    public String getContextIdentifier(HttpServletRequest request) {

        // The requests are correlated by a UUID which is generated at the beginning of the authentication flow
        // This method is to get the correlation ID from the request
        // In most cases this should be unchanged.
        return request.getParameter("sessionDataKey");
    }

    /**
     * Get the friendly name of the Authenticator
     */
    public String getFriendlyName() {

        return phoneNameVerifierAuthenticatorConstants.AUTHENTICATOR_FRIENDLY_NAME;
    }

    /**
     * Get the name of the Authenticator
     */
    public String getName() {

        return phoneNameVerifierAuthenticatorConstants.AUTHENTICATOR_NAME;
    }

    /**
     * Check mobile number and user name are correct.
     * If multiple/no user exits it returns null
     * @param httpServletRequest
     * @param userRealm
     * @return
     * @throws UserStoreException
     */
    private String validateMobileNumber(HttpServletRequest httpServletRequest, UserRealm userRealm)
            throws UserStoreException {

        String providedName = httpServletRequest.getParameter(INPUT_NAME);
        String providedMobileNo = httpServletRequest.getParameter(INPUT_MOBILE);
        UserStoreManager userStoreManager = (UserStoreManager) userRealm.getUserStoreManager();
        if (userStoreManager.isExistingUser(providedName)) {
            return getUsername(userStoreManager.getUserList(CLAIM_MOBILE, providedMobileNo, DEFAULT_PROFILE));
        }
        return null;
    }

    //extract username from given userList
    private String getUsername(String[] userList) {

        if (userList.length == 1) {
            return userList[0];
        }
        return null;
    }

    private String getSessionDataKey(AuthenticationContext context) {

        String[] queryParamList = context.getQueryParams().split("&");
        for (String val : queryParamList) {
            if (val.contains(SESSION_DATA_KEY)) {
                return val;
            }
        }
        return null;
    }
}
