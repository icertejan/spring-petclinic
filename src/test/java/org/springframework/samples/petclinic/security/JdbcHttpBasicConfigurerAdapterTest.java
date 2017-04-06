package org.springframework.samples.petclinic.security;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = PetClinicApplication.class)
public class JdbcHttpBasicConfigurerAdapterTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testNotAuthedNewOwner() {
        Owner owner = new Owner("John", "Doe", "1108 Jerry Dove Drive", "Lexington", "843-416-9698",
                Collections.<Pet> emptySet());

        HttpEntity<Owner> entity = new HttpEntity<Owner>(owner, buildHeaders());

        ResponseEntity<String> response = new TestRestTemplate().exchange(toUrl("/owners/new"), HttpMethod.POST, entity,
                String.class);

        assertThat(response.getStatusCodeValue(), equalTo(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void testAuthedWrongRoleNewOwner() {
        Owner owner = new Owner("John", "Doe", "1108 Jerry Dove Drive", "Lexington", "843-416-9698",
                Collections.<Pet> emptySet());

        HttpEntity<Owner> entity = new HttpEntity<Owner>(owner, buildHeaders());

        ResponseEntity<String> response = new TestRestTemplate().withBasicAuth("Bill", "Bill")
                .exchange(toUrl("/owners/new"), HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCodeValue(), equalTo(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testAuthedRightRoleNewOwner() {
        Owner owner = new Owner("John", "Doe", "1108 Jerry Dove Drive", "Lexington", "843-416-9698",
                Collections.<Pet> emptySet());

        HttpEntity<Owner> entity = new HttpEntity<Owner>(owner, buildHeaders());

        ResponseEntity<String> response = new TestRestTemplate().withBasicAuth("Samantha", "Samantha")
                .exchange(toUrl("/owners/new"), HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCodeValue(), equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void testAuthedListOwners() {
        HttpEntity<Object> entity = new HttpEntity<Object>(null, buildHeaders());

        ResponseEntity<String> response = new TestRestTemplate("Bill", "Bill").exchange(toUrl("/owners"),
                HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCodeValue(), equalTo(HttpStatus.OK.value()));
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    private String toUrl(String route) {
        return String.format("http://localhost:8085%s", route);
    }
}
