package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.config.websocket.WebSocketEventListener;
import com.anhoang.socialnetworkdemo.entity.Conversation;
import com.anhoang.socialnetworkdemo.entity.ConversationMember;
import com.anhoang.socialnetworkdemo.entity.Message;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.mapper.ConversationMapper;
import com.anhoang.socialnetworkdemo.model.message_chat.ConversationDto;
import com.anhoang.socialnetworkdemo.model.message_chat.ConversationFilter;
import com.anhoang.socialnetworkdemo.model.message_chat.MessageDto;
import com.anhoang.socialnetworkdemo.payload.PageData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.payload.socket_payload.MessageData;
import com.anhoang.socialnetworkdemo.repository.ConversationRepository;
import com.anhoang.socialnetworkdemo.repository.MessageRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.ConversationService;
import com.anhoang.socialnetworkdemo.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IConversationService implements ConversationService {
    private final AuthenticationUtils authUtils;
    private final UsersRepository usersRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private final MessageRepository messageRepository;
    private final WebSocketEventListener eventListener;

    @Override
    @Transactional
    public ResponseBody<?> userGetAllConversation(ConversationFilter filter) {
        try {
            Long userId = authUtils.getUserFromAuthentication().getId();
            Pageable pageable = PageRequest.of(
                    filter.getPageNumber() - 1, filter.getPageSize(), Sort.by(Sort.Order.desc("updatedAt")));
            Page<Conversation> page = conversationRepository.findAllByUserId(userId, pageable);
            List<ConversationDto> listChat = page.stream()
                    .map(conversation -> conversationMapper.entityToConversationDto(conversation, userId))
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .totalPage(page.getTotalPages())
                    .totalData(page.getTotalElements())
                    .pageSize(filter.getPageSize())
                    .pageNumber(filter.getPageNumber())
                    .data(listChat)
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Get list chat error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userCreatePrivateConversation(String userCode) {
        try {
            String myUserCode = authUtils.getUserFromAuthentication().getUserCode();
            Users me = usersRepository.findUsersByUserCode(myUserCode)
                    .orElseThrow(()-> new RequestNotFoundException("user not found!"));
            Users you = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(()-> new RequestNotFoundException("user not found!"));
            Conversation conversation = new Conversation();
            conversation.setActive(true);
            conversation.setType(Conversation.ConversationType.PRIVATE);
            ConversationMember meMember = new ConversationMember(conversation, me, false);
            ConversationMember youMember = new ConversationMember(conversation, you, false);
            conversation.setMembers(Set.of(meMember, youMember));
            conversationRepository.save(conversation);
            return new ResponseBody<>("");
        } catch (Exception e){
            log.error("Create chat error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userCreateGroupConversation(List<String> userCodeList) {
        try {
            String myUserCode = authUtils.getUserFromAuthentication().getUserCode();
            Users me = usersRepository.findUsersByUserCode(myUserCode)
                    .orElseThrow(()-> new RequestNotFoundException("user not found!"));
            Conversation conversation = new Conversation();
            conversation.setActive(true);
            conversation.setType(Conversation.ConversationType.GROUP);
            ConversationMember meMember = new ConversationMember(conversation, me, false);
            Set<ConversationMember> memberList = new HashSet<>();
            memberList.add(meMember);
            for (String youUserCode : userCodeList){
                Users you = usersRepository.findUsersByUserCode(youUserCode)
                        .orElseThrow(()-> new RequestNotFoundException("user not found!"));
                ConversationMember youMember = new ConversationMember(conversation, you, false);
                memberList.add(youMember);
            }
            conversation.setMembers(memberList);
            conversationRepository.save(conversation);
            return new ResponseBody<>("");
        } catch (Exception e){
            log.error("Create chat error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userGetMessageOfConversation(Long conversationId, int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Long isMember = conversationRepository.userExistedInConversation(conversationId, userId);
            if(isMember == null || isMember == 0){
                throw new RequestNotFoundException("User not in conversation!");
            }
            Conversation conversation = conversationRepository.findConversationById(conversationId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            ConversationDto dto = conversationMapper.entityToConversationDto(conversation, userId);
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("createdAt")));
            Page<Message> page = messageRepository.getListMessageByChatId(conversationId, pageable);
            List<MessageDto> dtoList = page.stream()
                    .map(message -> conversationMapper.entityToMessageDto(message,
                            authUtils.getUserFromAuthentication().getUserCode()))
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .totalPage(page.getTotalPages())
                    .totalData(page.getTotalElements())
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .data(dtoList)
                    .build();
            dto.setMessagePage(pageData);
            return new ResponseBody<>(dto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get message error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public void userSendMessage(MessageData req) {
        try {
            Users users = usersRepository.findUsersByUserCode(req.getUserCode())
                    .orElseThrow(() -> new RequestNotFoundException("USER_NOT_FOUND"));
            Conversation conversation = conversationRepository.findConversationById(req.getConversationId())
                    .orElseThrow(() -> new RequestNotFoundException("CHAT_NOT_FOUND"));
            Message message = new Message();
            message.setSenderUsers(users);
            message.setConversation(conversation);
            message.setContent(req.getContent());
            message.setMediaUrl(req.getMediaUrl());
            message.setIsRead(false);
            if(req.getReplyOf()!= null){
                Message parentMessage = messageRepository.findMessageById(req.getReplyOf())
                        .orElseThrow(() -> new RequestNotFoundException("PARENT_MESSAGE_NOT_FOUND"));
                message.setParentMessage(parentMessage);
            } else {
                message.setParentMessage(null);
            }
            message = messageRepository.save(message);
            conversation.setSendLastAt(message.getCreatedAt());
            conversation.setLatestMessage(message.getContent());
            conversationRepository.save(conversation);
        } catch (Exception e) {
            log.error("Send message error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Failed to send message");
        }
    }

    @Override
    @Transactional
    public void userSeenMessage(MessageData req) {
        try{
            Conversation conversation = conversationRepository.findById(req.getConversationId())
                    .orElseThrow(() -> new RequestNotFoundException("CHAT_NOT_FOUND"));
//            conversation.set(Se);
        } catch (Exception e){
            log.error("Send message error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Failed to send message");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userRemoveMessage(Long messageId) {
        try {
            Message message = messageRepository.findMessageById(messageId)
                    .orElseThrow(() -> new RequestNotFoundException("MESSAGE_NOT_FOUND"));
            messageRepository.delete(message);
            return new ResponseBody<>(null, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Remove message error! Error: " + e.getMessage());
            throw new RequestNotFoundException("Failed to remove message");
        }
    }
}
