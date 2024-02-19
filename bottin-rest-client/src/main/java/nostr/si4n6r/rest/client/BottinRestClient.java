package nostr.si4n6r.rest.client;

import lombok.Data;
import lombok.extern.java.Log;
import nostr.si4n6r.bottin.model.dto.BaseDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.logging.Level;

@Log
@Data
public class BottinRestClient<T extends BaseDto> {

    private final String entity;
    private final Class<T> clazz;
    private RestTemplate restTemplate;


    public BottinRestClient(String entity, Class<T> clazz) {
        this.entity = entity;
        this.clazz = clazz;
        this.restTemplate = new RestTemplate();
    }

    public T create(T dtoEntity) {
        HttpEntity<T> request = new HttpEntity<>(dtoEntity);
        log.log(Level.INFO, "Sending request: {0}", request);
        ResponseEntity<T> response = restTemplate
                .exchange(getBaseUrl(), HttpMethod.POST, request, clazz);
        log.log(Level.INFO, "Received response: {0}", response.toString());
        return response.getBody(); // This should now include the id if the server returns the created object
    }

    public T get(Long id) {
        String url = getBaseUrl() + "/" + id;
        log.log(Level.INFO, "Sending request: {0}", url);
        ResponseEntity<T> response = restTemplate.getForEntity(url, clazz);
        log.log(Level.INFO, "Received response: {0}", response.getBody());
        return response.getBody();

    }

    public void update(Long id, T dtoEntity) {
        String url = getBaseUrl() + "/" + id;
        HttpEntity<T> request = new HttpEntity<>(dtoEntity);
        log.log(Level.INFO, "Sending request: {0}", request);
        restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
        log.log(Level.INFO, "Updated entity with id: {0}", id);
    }

    public void delete(Long id) {
        String url = getBaseUrl() + "/" + id;
        log.log(Level.INFO, "Sending request to delete entity with id: {0}", id);
        restTemplate.delete(url);
        log.log(Level.INFO, "Deleted entity with id: {0}", id);
    }

    public List<T> getAll() {
        ResponseEntity<List<T>> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        log.log(Level.INFO, "Received response: {0}", response.getBody());
        return response.getBody();
    }

    public void updatePartial(Long id, T dtoEntity) {
        String url = getBaseUrl() + "/" + id;
        HttpEntity<T> request = new HttpEntity<>(dtoEntity);
        log.log(Level.INFO, "Sending request: {0}", request);
        restTemplate.exchange(url, HttpMethod.PATCH, request, Void.class);
        log.log(Level.INFO, "Updated entity with id: {0}", id);
    }

    public boolean exists(Long id) {
        String url = getBaseUrl() + "/" + id;
        log.log(Level.INFO, "Sending request: {0}", url);
        ResponseEntity<T> response = restTemplate.getForEntity(url, clazz);
        log.log(Level.INFO, "Received response: {0}", response.getBody());
        return response.getStatusCode().is2xxSuccessful();
    }

    public long count() {
        String url = UriComponentsBuilder.fromHttpUrl(getBaseUrl()).pathSegment("count").toUriString();
        log.log(Level.INFO, "Sending request: {0}", url);
        ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
        if (response == null) {
            return 0;
        }
        log.log(Level.INFO, "Received response: {0}", response.getBody());
        return response.getBody();
    }

    private String getBaseUrl() { //TODO - This is ugly
        return "http://localhost:6060/" + entity;
    }
}
