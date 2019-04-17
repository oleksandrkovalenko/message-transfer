package transfer.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import transfer.domain.Subscriber;
import transfer.dto.CreateSubscriberDTO;
import transfer.repository.SubscriberRepository;

import java.io.IOException;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static transfer.utils.TestUtils.createHeaders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sql/delete_data.sql")
public class SubscriberControllerTest {

    private static final String AUTH_TOKEN = "c66ae886-d6f0-4a75-aa30-3a5c3b432c9b";
    private static final String TEST_SUBSCRIBER = "Test Subscriber";

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Value("http://localhost:${local.server.port}/subscribers")
    private String url;

    @Test
    public void createSubscriber() throws IOException {
        CreateSubscriberDTO createSubscriberDTO = new CreateSubscriberDTO();
        createSubscriberDTO.setName(TEST_SUBSCRIBER);

        //@formatter:off
        String result = given()
            .headers(createHeaders(AUTH_TOKEN))
            .body(createSubscriberDTO)
        .when()
            .post(url)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .asString();
        //@formatter:on

        ObjectMapper objectMapper = new ObjectMapper();
        Subscriber subscriberDTO = objectMapper.readValue(result, Subscriber.class);

        assertThat(subscriberRepository.findAll().size(), is(1));
        Optional<Subscriber> subscriber = subscriberRepository.findById(subscriberDTO.getId());
        assertThat(subscriber.isPresent(), is(true));
        assertThat(subscriber.get().getName(), is(TEST_SUBSCRIBER));
    }

    @Test
    public void createSubscriberWithoutName() {
        CreateSubscriberDTO createSubscriberDTO = new CreateSubscriberDTO();

        //@formatter:off
        given()
            .headers(createHeaders(AUTH_TOKEN))
            .body(createSubscriberDTO)
        .when()
            .post(url)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
        //@formatter:on

    }

    @Test
    public void getSubscribers() {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(TEST_SUBSCRIBER);
        subscriberRepository.save(subscriber);

        //@formatter:off
        given()
            .headers(createHeaders(AUTH_TOKEN))
        .when()
            .get(url)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(1))
            .body("[0].id", is(subscriber.getId().toString()))
            .body("[0].name", is(TEST_SUBSCRIBER));
        //@formatter:on
    }

}
