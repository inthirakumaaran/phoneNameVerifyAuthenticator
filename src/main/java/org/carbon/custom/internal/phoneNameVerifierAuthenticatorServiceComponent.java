package org.carbon.custom.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.Deactivate;
import org.carbon.custom.phoneNameVerifierAuthenticator;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.Hashtable;

@Component(name = "org.carbon.custom.phoneNameVerifierAuthenticatorServiceComponent", immediate = true)

/**
 * OSGI service component class to register the local authenticator.
 */
public class phoneNameVerifierAuthenticatorServiceComponent {

    private static Log log = LogFactory.getLog(phoneNameVerifierAuthenticatorServiceComponent.class);
    private ServiceRegistration serviceRegistration = null;

    @Activate
    protected void activate(ComponentContext context) {

        //register the custom local authenticator as an OSGI service.
        serviceRegistration = context.getBundleContext().registerService(ApplicationAuthenticator.class.getName(),
        new phoneNameVerifierAuthenticator(), null);
        log.info("phoneNameVerifierAuthenticatorServiceComponent bundle activated successfully.");
    }

    protected void deactivate(ComponentContext context) {

        // Unregistering the custom local authenticator service.
        if(serviceRegistration != null) {
            if(log.isDebugEnabled()) {
                log.debug("phoneNameVerifierAuthenticatorServiceComponent is deactivated.");
            }
            serviceRegistration.unregister();
        }
    }

    @Reference(name = "user.realm.service.default",
              service =RealmService.class,
              cardinality = ReferenceCardinality.MANDATORY,
              policy = ReferencePolicy.DYNAMIC,
              unbind = "unsetRealmService")
    protected void setRealmService(RealmService realmService) {

        if (log.isDebugEnabled()) {
            log.debug("RealmService is set in the phoneNameVerifierAuthenticator bundle.");
        }

        phoneNameVerifierAuthenticatorDataHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {

        if (log.isDebugEnabled()) {
            log.debug("RealmService is unset in phoneNameVerifierAuthenticator bundle.");
        }
        phoneNameVerifierAuthenticatorDataHolder.getInstance().setRealmService(null);
    }

}
