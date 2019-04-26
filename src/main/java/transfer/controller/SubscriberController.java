package transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import transfer.dto.CreateSubscriberDTO;
import transfer.dto.SubscriberDTO;
import transfer.service.SubscriberService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @RequestMapping(method = RequestMethod.GET,  value = "/subscribers")
    public List<SubscriberDTO> list() {
        return subscriberService.getAllSubscribers();
    }

    @RequestMapping(method = RequestMethod.GET,  value = "/subscribers/{id}")
    public SubscriberDTO get(@PathVariable("id") UUID id) {
        return subscriberService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST,  value = "/subscribers")
    public SubscriberDTO get(@RequestBody @Valid CreateSubscriberDTO createSubscriberDTO) {
        return subscriberService.create(createSubscriberDTO);
    }

}
