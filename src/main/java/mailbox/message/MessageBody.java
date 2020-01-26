package mailbox.message;

import java.util.UUID;

public abstract class MessageBody {

    public abstract MessageBody copy();

    protected abstract boolean isOutdated();

}
