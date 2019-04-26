package transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import transfer.domain.Message;
import transfer.domain.Subscriber;
import transfer.dto.CreateMessageDTO;
import transfer.dto.MessageDTO;
import transfer.repository.MessageRepository;
import transfer.repository.SubscriberRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ConversionService conversionService;

    public List<MessageDTO> getAllForReceiver(UUID receiverId) {
        validateSubscriber(receiverId);
        List<Message> messages = messageRepository.findAllByReceiverId(receiverId);
        return messages.stream()
                .map(message -> conversionService.convert(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    public void createMessage(CreateMessageDTO createMessageDTO) {
        validateSubscriber(createMessageDTO.getReceiverId());
        validateSubscriber(createMessageDTO.getSenderId());
        Message message = conversionService.convert(createMessageDTO, Message.class);
        messageRepository.save(message);
    }

    private void validateSubscriber(UUID subscriberId) {
        Objects.requireNonNull(subscriberId);
        Optional<Subscriber> subscriber = subscriberRepository.findById(subscriberId);
        if (!subscriber.isPresent()) {
            throw new IllegalArgumentException(String.format("Subscriber not found with id %s", subscriberId));
        }
    }
}
