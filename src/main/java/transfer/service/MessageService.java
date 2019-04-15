package transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transfer.domain.Message;
import transfer.domain.Subscriber;
import transfer.dto.CreateMessageDTO;
import transfer.repository.MessageRepository;
import transfer.repository.SubscriberRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    public List<Message> getAllForReceiver(UUID receiverId) {
        validateSubscriber(receiverId);
        return messageRepository.findAllByReceiverId(receiverId);
    }

    public void createMessage(CreateMessageDTO createMessageDTO) {
        validateSubscriber(createMessageDTO.getReceiverId());

        Message message = new Message();
        message.setReceiverId(createMessageDTO.getReceiverId());
        message.setBody(createMessageDTO.getBody());

        messageRepository.save(message);
    }

    private void validateSubscriber(UUID receiverId) {
        Objects.requireNonNull(receiverId);
        Subscriber subscriber = subscriberRepository.getOne(receiverId);
        if (subscriber == null) {
            throw new IllegalArgumentException("Subscriber does not exist");
        }
    }
}
