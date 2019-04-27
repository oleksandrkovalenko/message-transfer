package transfer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transfer.domain.Message;
import transfer.domain.Subscriber;
import transfer.dto.CreateMessageDTO;
import transfer.dto.MessageDTO;
import transfer.error.NotFoundException;
import transfer.repository.MessageRepository;
import transfer.repository.SubscriberRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ConversionService conversionService;

    public List<MessageDTO> getAllForReceiver(UUID receiverId) {
        validateSubscriber(receiverId);
        List<Message> messages = messageRepository.findAllByReceiverIdAndReadIsFalse(receiverId);
        return messages.stream()
                .map(message -> conversionService.convert(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createMessage(CreateMessageDTO createMessageDTO) {
        validateSubscriber(createMessageDTO.getReceiverId());
        validateSubscriber(createMessageDTO.getSenderId());
        Message message = conversionService.convert(createMessageDTO, Message.class);
        messageRepository.save(message);
        log.info("Message saved");
        updateParentMessage(createMessageDTO.getParentMessageId());
    }

    private void updateParentMessage(UUID messageId) {
        if (messageId == null) {
            return;
        }
        log.info("Marking message with id {} as read", messageId);
        Message parentMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException(String.format("Parent message not found with id %s", messageId)));
        validateParentMessage(parentMessage);
        parentMessage.setRead(true);
        messageRepository.save(parentMessage);
    }

    private void validateParentMessage(Message parentMessage) {
        if (!parentMessage.isRead()) {
            return;
        }
        throw new IllegalArgumentException("parentMessage is already marked as read");
    }

    private void validateSubscriber(UUID subscriberId) {
        Objects.requireNonNull(subscriberId);
        Optional<Subscriber> subscriber = subscriberRepository.findById(subscriberId);
        if (!subscriber.isPresent()) {
            throw new IllegalArgumentException(String.format("Subscriber not found with id %s", subscriberId));
        }
    }
}
