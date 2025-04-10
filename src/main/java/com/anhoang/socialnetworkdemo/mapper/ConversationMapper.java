package com.anhoang.socialnetworkdemo.mapper;

import com.anhoang.socialnetworkdemo.config.Constant;
import com.anhoang.socialnetworkdemo.config.websocket.WebSocketEventListener;
import com.anhoang.socialnetworkdemo.entity.*;
import com.anhoang.socialnetworkdemo.model.media.MessageFileDto;
import com.anhoang.socialnetworkdemo.model.message_chat.ConversationDto;
import com.anhoang.socialnetworkdemo.model.message_chat.MemberDto;
import com.anhoang.socialnetworkdemo.model.message_chat.MessageDto;
import com.anhoang.socialnetworkdemo.repository.ConversationRepository;
import com.anhoang.socialnetworkdemo.repository.MessageRepository;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConversationMapper {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final WebSocketEventListener eventListener;
    private final String serverDomain;

    public ConversationMapper(ConversationRepository conversationRepository,
                              MessageRepository messageRepository,
                              WebSocketEventListener eventListener,
                              @Value("${server.domain}") String serverDomain) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.eventListener = eventListener;
        this.serverDomain = serverDomain;
    }

    public ConversationDto entityToConversationDto(Conversation conversation, Long userId){
        Set<ConversationMember> memberSet = conversation.getMembers();
        Set<MemberDto> memberDtoList = new HashSet<>();
        Users usersOther = null; boolean check = true;
        boolean userOtherOnline = false;
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
                userOtherOnline = dto.getOnline();
            }
        }
        if(conversation.getType()==Conversation.ConversationType.PRIVATE){
            return ConversationDto.builder()
                    .conversationId(conversation.getId())
                    .conversationAvatar(usersOther.getAvatar())
                    .conversationName(usersOther.getFullName())
                    .sendLastAt((conversation.getSendLastAt()!=null) ?
                            TimeMapperUtils.formatFacebookTime(conversation.getSendLastAt()) : null)
                    .lastMessage(conversation.getLatestMessage())
                    .unreadMessageCount(0)
                    .active(conversation.getActive())
                    .memberList(memberDtoList)
                    .type(conversation.getType())
                    .online(userOtherOnline)
                    .build();
        } else{
            return ConversationDto.builder()
                    .conversationId(conversation.getId())
                    .conversationAvatar(Constant.USER_IMAGE)
                    .conversationName("NEW GROUP")
                    .sendLastAt((conversation.getSendLastAt()!=null) ?
                            TimeMapperUtils.formatFacebookTime(conversation.getSendLastAt()) : null)
                    .lastMessage(conversation.getLatestMessage())
                    .unreadMessageCount(0)
                    .online(userOtherOnline)
                    .active(conversation.getActive())
                    .memberList(memberDtoList)
                    .build();
        }
    }

    public MessageFileDto entityToMessageFileDto(MessageFile file){
        return MessageFileDto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .fileNameSave(file.getFileNameSave())
                .fileSize(file.getFileSize())
                .fileType(file.getMediaType())
                .fileFormat(file.getMediaFormat())
                .fileUrl(serverDomain + "/api/guest/files/view/" + file.getFileNameSave())
                .downloadUrl(serverDomain + "/api/guest/files/download/" + file.getFileNameSave())
                .build();
    }

    public MessageDto entityToMessageDto(Message message, String userCode){
        Message parentMessage = message.getParentMessage();
        List<MessageFile> messageFileList = message.getMessageFiles();

        if(parentMessage!=null){
            MessageDto reply = new MessageDto();
            reply.setContent(parentMessage.getContent());
            reply.setMediaFiles(parentMessage.getMessageFiles().stream()
                    .map(this::entityToMessageFileDto).collect(Collectors.toList()));
            reply.setIsRead(false);
            reply.setCreatedAt(TimeMapperUtils.localDateTimeToTime24(parentMessage.getCreatedAt()));
            reply.setId(parentMessage.getId());
            reply.setUserCodeSend(parentMessage.getSenderUsers().getUserCode());
            reply.setUserAvatarSend(parentMessage.getSenderUsers().getAvatar());
            reply.setUserNameSend(parentMessage.getSenderUsers().getFullName());
            String senderUserCode = message.getSenderUsers().getUserCode();
            return MessageDto.builder()
                    .id(message.getId())
                    .content(message.getContent())
                    .mediaFiles(messageFileList.stream()
                            .map(this::entityToMessageFileDto).collect(Collectors.toList()))
                    .replyOf(reply)
                    .userCodeSend(senderUserCode)
                    .isMyMessage(userCode.equals(senderUserCode))
                    .userAvatarSend(message.getSenderUsers().getAvatar())
                    .userNameSend(message.getSenderUsers().getFullName())
                    .createdAt(TimeMapperUtils.localDateTimeToTime24(message.getCreatedAt()))
                    .isRead(message.getIsRead())
                    .build();
        } else {
            String senderUserCode = message.getSenderUsers().getUserCode();
            return MessageDto.builder()
                    .id(message.getId())
                    .content(message.getContent())
                    .mediaFiles(messageFileList.stream()
                            .map(this::entityToMessageFileDto).collect(Collectors.toList()))
                    .replyOf(null)
                    .userCodeSend(senderUserCode)
                    .isMyMessage(userCode.equals(senderUserCode))
                    .userAvatarSend(message.getSenderUsers().getAvatar())
                    .userNameSend(message.getSenderUsers().getFullName())
                    .createdAt(TimeMapperUtils.localDateTimeToTime24(message.getCreatedAt()))
                    .isRead(message.getIsRead())
                    .build();
        }

    }
}
