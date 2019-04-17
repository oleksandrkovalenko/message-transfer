package transfer.controller;


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
import transfer.domain.Message;
import transfer.domain.Subscriber;
import transfer.dto.CreateMessageDTO;
import transfer.repository.MessageRepository;
import transfer.repository.SubscriberRepository;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static transfer.utils.TestUtils.createHeaders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sql/delete_data.sql")
public class MessageControllerTest {

    private static final String AUTH_TOKEN = "c66ae886-d6f0-4a75-aa30-3a5c3b432c9b";

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Value("http://localhost:${local.server.port}/messages")
    private String url;

    @Test
    public void createMessage() {
        Subscriber receiver = createSubscriber("Receiver");
        Subscriber sender = createSubscriber("Send");

        CreateMessageDTO createMessageDTO = new CreateMessageDTO();
        createMessageDTO.setReceiverId(receiver.getId());
        createMessageDTO.setSenderId(sender.getId());
        createMessageDTO.setBody("Message Body");

        //@formatter:off
        given()
            .headers(createHeaders(AUTH_TOKEN))
            .body(createMessageDTO)
        .when()
            .post(url)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.CREATED.value());
        //@formatter:on

        List<Message> messages = messageRepository.findAll();
        assertThat(messages.size(), is(1));
        assertThat(messages.get(0).getReceiverId(), is(receiver.getId()));
        assertThat(messages.get(0).getSenderId(), is(sender.getId()));
    }

    @Test
    public void createMessageInvalidToken() {
        CreateMessageDTO createMessageDTO = new CreateMessageDTO();
        createMessageDTO.setReceiverId(UUID.randomUUID());
        createMessageDTO.setSenderId(UUID.randomUUID());
        createMessageDTO.setBody("Message Body");

        //@formatter:off
        given()
            .headers(createHeaders("Invalid"))
            .body(createMessageDTO)
        .when()
            .post(url)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
        //@formatter:on
    }

    @Test
    public void createMessageInvalidReceiver() {
        CreateMessageDTO createMessageDTO = new CreateMessageDTO();
        createMessageDTO.setReceiverId(UUID.randomUUID());
        createMessageDTO.setSenderId(UUID.randomUUID());
        createMessageDTO.setBody("Message Body");

        //@formatter:off
        given()
            .headers(createHeaders(AUTH_TOKEN))
            .body(createMessageDTO)
        .when()
            .post(url)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.CONFLICT.value());
        //@formatter:on
    }

    @Test
    public void listOfMessages() {
        String MESSAGE_BODY = "Message to be received";
        Subscriber subscriber = createSubscriber("Subscriber");

        Message message = new Message();
        message.setReceiverId(UUID.randomUUID());
        message.setSenderId(UUID.randomUUID());
        message.setBody("Message to other receiver");
        messageRepository.save(message);

        message = new Message();
        message.setReceiverId(subscriber.getId());
        message.setSenderId(UUID.randomUUID());
        message.setBody(MESSAGE_BODY);
        messageRepository.save(message);

        //@formatter:off
        given()
            .headers(createHeaders(AUTH_TOKEN))
        .when()
            .get(url + "/" + subscriber.getId())
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(1))
            .body("[0].body", is(MESSAGE_BODY));
        //@formatter:on
    }

    @Test
    public void listOfMessagesInvalidToken() {
        Subscriber subscriber = createSubscriber("Subscriber");

        //@formatter:off
        given()
            .headers(createHeaders(""))
        .when()
            .get(url + "/" + subscriber.getId())
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
        //@formatter:on
    }

    @Test
    public void listOfMessagesRandomToken() {
        Subscriber subscriber = createSubscriber("Subscriber");

        //@formatter:off
        given()
            .headers(createHeaders(UUID.randomUUID().toString()))
        .when()
            .get(url + "/" + subscriber.getId())
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
        //@formatter:on
    }

    @Test
    public void listOfMessagesNullToken() {
        Subscriber subscriber = createSubscriber("Subscriber");

        //@formatter:off
        given()
            .headers(createHeaders(null))
        .when()
            .get(url + "/" + subscriber.getId())
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
        //@formatter:on
    }

    private Subscriber createSubscriber(String name) {
        Subscriber receiver = new Subscriber();
        receiver.setName(name);
        subscriberRepository.save(receiver);
        return receiver;
    }
}
