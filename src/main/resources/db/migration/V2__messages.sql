create table messages
(
    id uuid,
    receiver_id uuid not null,
    sender_id uuid not null,
    body text not null,
    read boolean,
    created datatime
)
