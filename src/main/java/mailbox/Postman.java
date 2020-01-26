package mailbox;

import mailbox.message.Message;

import java.util.*;

public class Postman {

    private static final HashMap<Mailable, TreeSet<Message>> mailBox = new HashMap<>();

    private static Long counter = 0L;
    private Postman() {
    }

    /**
     * Deposit the message on the corresponding mailbox. Create it if necessary.
     * @param message Message to deposit.
     */
    public static synchronized void mail(Message message) {
        counter = counter++ % 30;
        Mailable recipient = message.getRecipient();

        TreeSet<Message> messages = mailBox.get(recipient);
        // If set does not existe, create it.
        if (messages == null) {
            messages = new TreeSet<>();
            messages.add(message);
            mailBox.put(recipient, messages);
        } else {
            // Add if item is not already in list
            messages.add(message);
        }

        if (counter == 0)
        messages.removeIf(Message::isOutdated);
    }

    public static synchronized void forward(Message message, Mailable... cc) {
        Arrays.stream(cc)
                .forEach(mailable -> mail(message));
    }

    public static synchronized Message getLastMessage(Mailable mailable) {
        return mailBox.get(mailable).pollFirst();
    }

    public static synchronized Set<Message> getMessages(Mailable mailable) {
        return mailBox.remove(mailable);
    }

}
