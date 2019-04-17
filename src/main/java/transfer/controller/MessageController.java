package transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import transfer.domain.Message;
import transfer.dto.CreateMessageDTO;
import transfer.service.MessageService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(method = RequestMethod.GET, value = "/messages/{receiverId}")
    public List<Message> getAllForReceiver(@PathVariable("receiverId") UUID receiverId) {
        return messageService.getAllForReceiver(receiverId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/messages")
    public void createMessage(@RequestBody @Valid CreateMessageDTO createMessageDTO) {
        messageService.createMessage(createMessageDTO);
    }

}
