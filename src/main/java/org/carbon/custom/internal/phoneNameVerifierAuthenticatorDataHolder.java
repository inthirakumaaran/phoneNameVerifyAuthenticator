package org.carbon.custom.internal;

import org.wso2.carbon.user.core.service.RealmService;

public class phoneNameVerifierAuthenticatorDataHolder {

    private static phoneNameVerifierAuthenticatorDataHolder instance = new phoneNameVerifierAuthenticatorDataHolder();

    private RealmService realmService;


    private phoneNameVerifierAuthenticatorDataHolder() {

    }

    public static phoneNameVerifierAuthenticatorDataHolder getInstance() {

        return instance;
    }

    public RealmService getRealmService() {

        if (realmService == null) {
            throw new RuntimeException("phoneName Verifier Authenticator did not start properly.");
        }
        return realmService;
    }

    public void setRealmService(RealmService realmService) {

        this.realmService = realmService;
    }

}
