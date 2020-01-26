package mailbox;

import mailbox.message.Message;

public interface Mailable {

    void sendMessage(Message message);

}
