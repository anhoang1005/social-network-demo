package com.anhoang.socialnetworkdemo.mapper;

import com.anhoang.socialnetworkdemo.config.Constant;
import com.anhoang.socialnetworkdemo.config.websocket.WebSocketEventListener;
import com.anhoang.socialnetworkdemo.entity.Conversation;
import com.anhoang.socialnetworkdemo.entity.ConversationMember;
import com.anhoang.socialnetworkdemo.entity.Message;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.model.message_chat.ConversationDto;
import com.anhoang.socialnetworkdemo.model.message_chat.MemberDto;
import com.anhoang.socialnetworkdemo.model.message_chat.MessageDto;
import com.anhoang.socialnetworkdemo.repository.ConversationRepository;
import com.anhoang.socialnetworkdemo.repository.MessageRepository;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class ConversationMapper {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final WebSocketEventListener eventListener;

    public ConversationDto entityToConversationDto(Conversation conversation, Long userId){
        Set<ConversationMember> memberSet = conversation.getMembers();
        Set<MemberDto> memberDtoList = new HashSet<>();
        Users usersOther = null; boolean check = true;
        for (ConversationMember member : memberSet){
            Users users = member.getUsers();
            MemberDto dto = new MemberDto();
            dto.setUserCode(users.getUserCode());
            dto.setFullName(users.getFullName());
            dto.setAvatar(users.getAvatar());
            dto.setIsConversationAdmin(member.getIsConversationAdmin());
            dto.setOnline(eventListener.checkCustomerConnection(users.getUserCode()));
            memberDtoList.add(dto);
            if(check && !users.getId().equals(userId)){
                check = false;
                usersOther = users;
            }
        }
        if(conversation.getType()==Conversation.ConversationType.PRIVATE){
            return ConversationDto.builder()
                    .conversationId(conversation.getId())
                    .conversationAvatar(usersOther.getAvatar())
                    .conversationName(usersOther.getFullName())
                    .sendLastAt((conversation.getSendLastAt()!=null) ?
                            TimeMapperUtils.localDateTimeToHouseString(conversation.getSendLastAt()) : null)
                    .lastMessage(conversation.getLatestMessage())
                    .unreadMessageCount(0)
                    .active(conversation.getActive())
                    .memberList(memberDtoList)
                    .build();
        } else{
            return ConversationDto.builder()
                    .conversationId(conversation.getId())
                    .conversationAvatar(Constant.USER_IMAGE)
                    .conversationName("NEW GROUP")
                    .sendLastAt((conversation.getSendLastAt()!=null) ?
                            TimeMapperUtils.localDateTimeToHouseString(conversation.getSendLastAt()) : null)
                    .lastMessage(conversation.getLatestMessage())
                    .unreadMessageCount(0)
                    .active(conversation.getActive())
                    .memberList(memberDtoList)
                    .build();
        }
    }

    public MessageDto entityToMessageDto(Message message){
        Message parentMessage = message.getParentMessage();
        if(parentMessage!=null){
            MessageDto reply = new MessageDto();
            reply.setContent(parentMessage.getContent());
            reply.setImageUrl(parentMessage.getMediaUrl());
            reply.setIsRead(false);
            reply.setCreatedAt(TimeMapperUtils.localDateTimeToHouseString(parentMessage.getCreatedAt()));
            reply.setId(parentMessage.getId());
            reply.setUserCodeSend(parentMessage.getSenderUsers().getUserCode());
            reply.setUserAvatarSend(parentMessage.getSenderUsers().getAvatar());
            return MessageDto.builder()
                    .id(message.getId())
                    .content(message.getContent())
                    .imageUrl(message.getMediaUrl())
                    .replyOf(reply)
                    .userCodeSend(message.getSenderUsers().getUserCode())
                    .userAvatarSend(message.getSenderUsers().getAvatar())
                    .createdAt(TimeMapperUtils.localDateTimeToHouseString(message.getCreatedAt()))
                    .isRead(message.getIsRead())
                    .build();
        } else {
            return MessageDto.builder()
                    .id(message.getId())
                    .content(message.getContent())
                    .imageUrl(message.getMediaUrl())
                    .replyOf(null)
                    .userCodeSend(message.getSenderUsers().getUserCode())
                    .userAvatarSend(message.getSenderUsers().getAvatar())
                    .createdAt(TimeMapperUtils.localDateTimeToHouseString(message.getCreatedAt()))
                    .isRead(message.getIsRead())
                    .build();
        }

    }
}
