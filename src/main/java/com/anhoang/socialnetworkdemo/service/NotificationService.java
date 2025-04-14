package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.entity.Notifications;
import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.anhoang.socialnetworkdemo.model.notify.PostCommentNotifyDto;
import com.anhoang.socialnetworkdemo.model.notify.PostNotifyDto;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;

import java.time.LocalDateTime;

public interface NotificationService {

    void sendFriendshipNotifyToUser(Notifications.Type type,
                                    String senderCode,
                                    String userCode,
                                    LocalDateTime createdAt);

    void sendSystemNotifyToUser(String userCode,
                                String content,
                                LocalDateTime createdAt);

    void sendPostNotifyToUser(Notifications.Type type,
                              String toUserCode,
                              Long postId,
                              PostNotifyDto postNotifyDto,
                              LocalDateTime createdAt);

    void sendPostCommentNotifyToUser(Notifications.Type type,
                                     String toUserCode,
                                     Long commentId,
                                     PostCommentNotifyDto postCommentNotifyDto,
                                     LocalDateTime createdAt);

    ResponseBody<?> userGetNotifyList();

    ResponseBody<?> userGetInfoData();
}
