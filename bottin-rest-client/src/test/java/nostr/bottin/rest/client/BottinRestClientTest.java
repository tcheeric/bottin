package nostr.bottin.rest.client;

import nostr.bottin.model.dto.RelayDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class BottinRestClientTest {

    @Mock
    private RestTemplate restTemplate;

    private BottinRestClient<RelayDto> restClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        restClient = new BottinRestClient<>("relay", RelayDto.class);
        restClient.setRestTemplate(restTemplate);
    }

    @Test
    public void testCreate() {
        long index = System.currentTimeMillis();
        RelayDto expectedEntity = new RelayDto("relay_" + index, "url_" + index, new ArrayList<>());
        ResponseEntity<RelayDto> responseEntity = ResponseEntity.ok(expectedEntity);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(RelayDto.class)))
                .thenReturn(responseEntity);

        RelayDto actualEntity = restClient.create(new RelayDto("relay_" + index, "url_"+index, new ArrayList<>()));

        assertEquals(expectedEntity, actualEntity);
    }

    @Test
    public void testGet() {
        long index = System.currentTimeMillis();
        RelayDto expectedEntity = new RelayDto("relay_" + index, "url_" + index, new ArrayList<>());
        ResponseEntity<RelayDto> responseEntity = ResponseEntity.ok(expectedEntity);
        when(restTemplate.getForEntity(any(String.class), eq(RelayDto.class))).thenReturn(responseEntity);

        RelayDto actualEntity = restClient.get(1L);

        assertEquals(expectedEntity, actualEntity);
    }

    @Test
    public void testUpdate() {
        long index = System.currentTimeMillis();
        RelayDto entity = new RelayDto("relay_" + index, "url_" + index, new ArrayList<>());
        restClient.update(1L, entity);
    }

    @Test
    public void testDelete() {
        restClient.delete(1L);
    }

/*
    @Test
    public void testGetAll() {
        long index = System.currentTimeMillis();
        RelayDto entity = new RelayDto("relay_" + index, "url_" + index, new ArrayList<>());
        RelayDto[] expectedEntities = new RelayDto[]{entity};
        ResponseEntity<RelayDto[]> responseEntity = ResponseEntity.ok(expectedEntities);
        when(restTemplate.getForEntity(any(String.class), eq(RelayDto[].class))).thenReturn(responseEntity);

        List<RelayDto> actualEntities = restClient.getAll();

        assertEquals(Arrays.asList(expectedEntities), actualEntities);
    }
*/

    @Test
    public void testUpdatePartial() {
        long index = System.currentTimeMillis();
        RelayDto entity = new RelayDto("relay_" + index, "url_" + index, new ArrayList<>());
        restClient.updatePartial(1L, entity);
    }

    @Test
    public void testExists() {
        long index = System.currentTimeMillis();
        RelayDto entity = new RelayDto("relay_" + index, "url_" + index, new ArrayList<>());
        ResponseEntity<RelayDto> responseEntity = ResponseEntity.ok(entity);
        when(restTemplate.getForEntity(any(String.class), eq(RelayDto.class))).thenReturn(responseEntity);

        boolean exists = restClient.exists(1L);

        assertTrue(exists);
    }

    @Test
    public void testCount() {
        ResponseEntity<Long> responseEntity = ResponseEntity.ok(1L);
        when(restTemplate.getForEntity(any(String.class), eq(Long.class))).thenReturn(responseEntity);

        long count = restClient.count();

        assertEquals(1L, count);
    }

}