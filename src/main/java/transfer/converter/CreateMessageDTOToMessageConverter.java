package transfer.converter;

import org.springframework.core.convert.converter.Converter;
import transfer.domain.Message;
import transfer.dto.CreateMessageDTO;

public class CreateMessageDTOToMessageConverter implements Converter<CreateMessageDTO, Message> {
    @Override
    public Message convert(CreateMessageDTO createMessageDTO) {
        Message message = new Message();
        message.setReceiverId(createMessageDTO.getReceiverId());
        message.setSenderId(createMessageDTO.getSenderId());
        message.setBody(createMessageDTO.getBody());
        return message;
    }
}
