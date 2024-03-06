package nostr.bottin.rest.controller;

import lombok.extern.java.Log;
import nostr.bottin.model.NostrIdentity;
import nostr.bottin.rest.repository.NostrIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;

@RestController
@RequestMapping("/identity")
@Log
public class NostrIdentityController {

    @Autowired
    private NostrIdentityRepository nostrIdentityRepository;

    @GetMapping("/publicKey/{publicKey}")
    public ResponseEntity<NostrIdentity> getIdentityByPublicKey(@PathVariable("publicKey") String publicKey) {
        log.log(Level.INFO, "Looking for identity with publicKey: {0}", publicKey);
        var identity = nostrIdentityRepository.findByPublicKey(publicKey);
        log.log(Level.INFO, "Found identity: {0}", identity);
        if (identity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(identity, HttpStatus.OK);
        }
    }
}
