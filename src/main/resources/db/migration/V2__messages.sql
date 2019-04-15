create table messages
(
    id uuid,
    receiver_id uuid,
    body text,
    read boolean,
    created datatime
)
