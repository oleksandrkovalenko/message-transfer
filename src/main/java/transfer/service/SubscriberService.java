package transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transfer.domain.Subscriber;
import transfer.dto.CreateSubscriberDTO;
import transfer.repository.SubscriberRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    public Subscriber get(UUID id) {
        return Optional.ofNullable(subscriberRepository.getOne(id)).orElseThrow(NullPointerException::new);
    }

    public Subscriber create(CreateSubscriberDTO createSubscriberDTO) {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(createSubscriberDTO.getName());
        return subscriberRepository.save(subscriber);
    }
}
