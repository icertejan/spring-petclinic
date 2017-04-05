package org.springframework.samples.petclinic.security;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes=PetClinicApplication.class)
public class JdbcHttpBasicConfigurerAdapterTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testNotAuthedNewOwner() {
        MultiValueMap<String, String> headers = buildHeaders();

        HashMap<String, String> postBody = new HashMap<String, String>();

        HttpEntity<?> requestLogin = new HttpEntity<>(postBody, headers);

        exception.expect(HttpClientErrorException.class);
        new RestTemplate().postForEntity(url("/owners/new"), requestLogin, String.class);
    }
    
    @Test
    public void testAuthedAsBillNewOwner() {
        MultiValueMap<String, String> headers = buildHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = new TestRestTemplate("Bill", "Bill").exchange(
                url("/owners"),
                HttpMethod.GET, entity, String.class);
        
        assertThat(response.getStatusCodeValue(), equalTo(HttpStatus.OK.value()));
    }

    private MultiValueMap<String, String> buildHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.put("Content-Type", Arrays.asList("application/json"));
        return headers;
    }
    
    private String url(String route) {
        return String.format("http://localhost:8085%s", route);
    }
}
