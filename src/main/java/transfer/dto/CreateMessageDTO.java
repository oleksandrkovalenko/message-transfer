package transfer.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CreateMessageDTO {
    @NotNull
    private UUID receiverId;
    @NotNull
    private UUID senderId;
    @NotBlank
    private String body;
}
