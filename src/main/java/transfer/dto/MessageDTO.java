package transfer.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class MessageDTO {
    private UUID id;
    private UUID receiverId;
    private UUID senderId;
    private String body;
    private boolean read;
    private Date created;

}
