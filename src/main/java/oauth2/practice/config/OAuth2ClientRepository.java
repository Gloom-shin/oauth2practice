package oauth2.practice.config;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.stereotype.Repository;

@Repository
public class OAuth2ClientRepository implements ClientRegistrationRepository {
    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return ClientRegistrations.fromIssuerLocation("https://idp.example.com/issuer").build();
    }
}
