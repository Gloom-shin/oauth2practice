package oauth2.practice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2ClientController {
    @Autowired
    private ClientRegistrationRepository repository;

    @GetMapping
    public HttpEntity<?> index(){

        ClientRegistration clientRegistration = repository.findByRegistrationId("google");

        return new ResponseEntity<ClientRegistration>(HttpStatus.OK);
    }

}
