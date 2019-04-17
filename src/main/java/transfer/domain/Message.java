package transfer.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID receiverId;
    private UUID senderId;
    private String body;
    private boolean read;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

}
