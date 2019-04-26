package transfer.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SubscriberDTO {
    private UUID id;
    private String name;
}
