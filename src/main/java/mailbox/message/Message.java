package mailbox.message;

import mailbox.Mailable;
import mailbox.message.type.MessageType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Comparable<Message> {

    private static Long numberMessages = 0L;

    private final Long id;
    private Mailable sender;
    private Mailable recipient;
    private LocalDateTime time;
    private MessageBody body;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");

    private Message() {
        numberMessages++;
        this.id = numberMessages;
    }

    private Message (Long id, Mailable sender, Mailable recipient, MessageBody body) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.time = LocalDateTime.now();
        this.body = body;
    }

    public Message(Mailable sender, Mailable recipient, MessageBody body) {
        numberMessages++;

        this.id = numberMessages;
        this.sender = sender;
        this.recipient = recipient;
        this.time = LocalDateTime.now();
        this.body = body;
    }

    protected Message copy() {
        return new Message(this.id, this.sender, this.recipient, this.body.copy());
    }

    public boolean isOutdated() {
        return this.getBody().isOutdated();
    }

    // Trier par ordre décroissant d'ID (derniers envoyé --> premier lu).
    @Override
    public int compareTo(Message o) {
        return - id.compareTo(o.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return (id.equals(message.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("(%d) From %s to %s at %s: (%s) %s",
                id,
                sender,
                recipient,
                time.format(formatter),
                body.getClass().getAnnotation(MessageType.class).type(),
                body);
    }

    public Long getId() {
        return id;
    }

    public Mailable getSender() {
        return sender;
    }

    public void setSender(Mailable sender) {
        this.sender = sender;
    }

    public Mailable getRecipient() {
        return recipient;
    }

    public void setRecipient(Mailable recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

}
