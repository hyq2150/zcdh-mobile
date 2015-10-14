package com.huanxin;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;

/**
 * User: xyh
 * Date: 2015/9/16
 * Time: 13:55
 */
public class HXMsgHelper {

    /**
     * 获取环信未读消息数
     *
     * @return
     */
    public static int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations()
                .values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
            }
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }
}
