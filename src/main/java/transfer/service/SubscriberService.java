package transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import transfer.domain.Subscriber;
import transfer.dto.CreateSubscriberDTO;
import transfer.dto.SubscriberDTO;
import transfer.error.NotFoundException;
import transfer.repository.SubscriberRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ConversionService conversionService;

    public List<SubscriberDTO> getAllSubscribers() {
        return subscriberRepository.findAll().stream()
                .map(subscriber -> conversionService.convert(subscriber, SubscriberDTO.class))
                .collect(Collectors.toList());
    }

    public SubscriberDTO get(UUID id) {
        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Subscriber not found by %s", id)));
        return conversionService.convert(subscriber, SubscriberDTO.class);
    }

    public SubscriberDTO create(CreateSubscriberDTO createSubscriberDTO) {
        Subscriber subscriber = conversionService.convert(createSubscriberDTO, Subscriber.class);
        Subscriber savedSubscriber = subscriberRepository.save(subscriber);
        return conversionService.convert(savedSubscriber, SubscriberDTO.class);
    }
}
