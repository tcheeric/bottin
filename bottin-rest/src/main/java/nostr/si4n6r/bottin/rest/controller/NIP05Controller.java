package nostr.si4n6r.bottin.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.si4n6r.bottin.model.NostrIdentity;
import nostr.si4n6r.bottin.rest.repository.NostrIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;

@RestController
@RequestMapping("/nip05")
@Log
public class NIP05Controller {

    @Autowired
    private NostrIdentityRepository nostrIdentityRepository;

    @GetMapping
    // TODO - Use path variables instead of request parameters
    public ResponseEntity<String> get(@RequestParam("localpart") String localpart, @RequestParam("domain") String domain) {
        try {
            var identity = nostrIdentityRepository.findByLocalpartAndDomain(localpart, domain);
            if (identity == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                var json = identityDtoToJson(identity);
                return new ResponseEntity<>(json, HttpStatus.OK);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String identityDtoToJson(@NonNull NostrIdentity nostrIdentity) throws JsonProcessingException {
        var mapper = new ObjectMapper();

        // Create root JSON object
        var jsonObject = mapper.createObjectNode();

        // Add names to the JSON object
        var names = jsonObject.putObject("names");
        names.put(nostrIdentity.getLocalpart(), nostrIdentity.getPublicKey());

        // Add relays to the JSON object
        var relays = jsonObject.putObject("relays");
        var relayUrls = relays.putArray(nostrIdentity.getPublicKey());
        for (var relay : nostrIdentity.getRelays()) {
            log.log(Level.INFO, "Adding relay: " + relay);
            relayUrls.add(relay.getUrl());
        }

        var json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        log.log(Level.INFO, "result: {0}", json);
        return json;
    }
}
