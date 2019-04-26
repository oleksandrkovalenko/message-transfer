package transfer.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import transfer.domain.Subscriber;
import transfer.dto.SubscriberDTO;

public class SubscriberToSubscriberDTOConverter implements Converter<Subscriber, SubscriberDTO> {
    @Override
    public SubscriberDTO convert(Subscriber subscriber) {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        BeanUtils.copyProperties(subscriber, subscriberDTO);
        return subscriberDTO;
    }
}
