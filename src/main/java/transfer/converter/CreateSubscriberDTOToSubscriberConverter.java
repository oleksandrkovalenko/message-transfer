package transfer.converter;

import org.springframework.core.convert.converter.Converter;
import transfer.domain.Subscriber;
import transfer.dto.CreateSubscriberDTO;

public class CreateSubscriberDTOToSubscriberConverter implements Converter<CreateSubscriberDTO, Subscriber> {
    @Override
    public Subscriber convert(CreateSubscriberDTO createSubscriberDTO) {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(createSubscriberDTO.getName());
        return subscriber;
    }
}
