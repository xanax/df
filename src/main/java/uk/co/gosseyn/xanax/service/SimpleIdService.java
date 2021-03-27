package uk.co.gosseyn.xanax.service;

import org.springframework.context.annotation.Profile;

import javax.inject.Named;
import java.util.UUID;

@Named
@Profile("!bitcoin")
public class SimpleIdService implements IdService {

    public String newAddress() {
        return UUID.randomUUID().toString();
    }
}
