package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.model.Message;
import com.jxg.isn_backend.model.User;

public class UserWithRecentMessageDTO {
    private User user;
    private Message recentMessage;
    private boolean isRead;

    public UserWithRecentMessageDTO(User user, Message recentMessage) {
        this.user = user;
        this.recentMessage = recentMessage;
        this.isRead = recentMessage != null && recentMessage.isRead();
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}
