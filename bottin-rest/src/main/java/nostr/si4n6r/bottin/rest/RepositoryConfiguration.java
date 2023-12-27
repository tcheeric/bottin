package nostr.si4n6r.bottin.rest;

import nostr.si4n6r.bottin.model.NostrIdentity;
import nostr.si4n6r.bottin.model.Relay;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class RepositoryConfiguration implements RepositoryRestConfigurer {
}