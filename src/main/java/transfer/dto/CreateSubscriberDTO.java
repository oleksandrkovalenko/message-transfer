package transfer.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateSubscriberDTO {
    @NotNull
    private String name;
}
