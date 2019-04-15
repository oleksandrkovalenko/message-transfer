package transfer.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateMessageDTO {
    private UUID receiverId;
    private String body;
}
