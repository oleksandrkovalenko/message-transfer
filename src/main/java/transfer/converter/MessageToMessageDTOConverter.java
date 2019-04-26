package transfer.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import transfer.domain.Message;
import transfer.dto.MessageDTO;

public class MessageToMessageDTOConverter implements Converter<Message, MessageDTO> {
    @Override
    public MessageDTO convert(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        BeanUtils.copyProperties(message, messageDTO);
        return messageDTO;
    }
}
