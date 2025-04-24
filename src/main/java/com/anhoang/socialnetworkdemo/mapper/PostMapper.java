package com.anhoang.socialnetworkdemo.mapper;

import com.anhoang.socialnetworkdemo.entity.MediaFile;
import com.anhoang.socialnetworkdemo.entity.Post;
import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.model.media.MediaDto;
import com.anhoang.socialnetworkdemo.model.post.PostDto;
import com.anhoang.socialnetworkdemo.model.post.SharedPostDto;
import com.anhoang.socialnetworkdemo.repository.PostReactionRepository;
import com.anhoang.socialnetworkdemo.repository.PostRepository;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PostMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PostReactionRepository reactionRepository;
    private final PostRepository postRepository;

    public String convertToJson(List<String> strings) {
        try {
            return objectMapper.writeValueAsString(strings);
        } catch (JsonProcessingException e) {
            throw new RequestNotFoundException("Error convert");
        }
    }

    public List<String> convertFromJson(String json){
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RequestNotFoundException("Error convert");
        }
    }

    public PostDto entityToPostDto(Post post, String userCode){
        List<MediaFile> mediaFileList = post.getMediaFiles();
        PostDto dto = new PostDto();
        Users users = post.getUsers();
        dto.setId(post.getId());
        dto.setPostAvatar(users.getAvatar());
        dto.setPostName(users.getFullName());
        dto.setUserCode(users.getUserCode());
        dto.setContent(post.getContent()!=null ? post.getContent() : null);
        dto.setHashTag(post.getHashtag() != null ? convertFromJson(post.getHashtag()): null);
        dto.setLocation(post.getLocation()!=null ? post.getLocation() : null);
        dto.setStatus(post.getStatus());
        dto.setPostType(post.getPostType());
        dto.setLikeCount(postRepository.countReactionsByPostId(post.getId()));
        dto.setCommentCount(postRepository.countCommentsByPostId(post.getId()));
        dto.setSharedCount(postRepository.countSharesByPostId(post.getId()));
        dto.setIsShared(post.getIsShared());
        dto.setVisibility(post.getVisibility());
        dto.setCreatedAt(TimeMapperUtils.formatFacebookTime(post.getCreatedAt()));

        if (mediaFileList != null && !mediaFileList.isEmpty()) {
            dto.setMediaFiles(mediaFileList.stream().map(mediaFile -> {
                        MediaDto mediaDto = new MediaDto();
                        mediaDto.setMediaType(mediaFile.getMediaType());
                        mediaDto.setFileUrl(mediaFile.getUrl());
                        mediaDto.setFileSize(mediaFile.getFileSize());
                        mediaDto.setFormat(mediaFile.getMediaFormat());
                        return mediaDto; }).toList());
        } else {
            dto.setMediaFiles(null);
        }

        if(post.getIsShared()) dto.setSharedPost(entityToSharePostDto(post.getSharedPost()));
        else dto.setSharedPost(null);

        if(userCode!=null){
            PostReaction reaction = reactionRepository.findPostReactionByPostIdAndUserCode(
                    userCode, post.getId());
            if(reaction!=null){
                dto.setMyReaction(reaction.getReaction());
            } else {
                dto.setMyReaction(PostReaction.Reaction.NONE);
            }
        } else {
            dto.setMyReaction(null);
        }
        return dto;
    }

    public SharedPostDto entityToSharePostDto(Post post){
        List<MediaFile> mediaFileList = post.getMediaFiles();
        Users users = post.getUsers();
        SharedPostDto dto = new SharedPostDto();
        dto.setContent(post.getContent()!=null ? post.getContent() : null);
        dto.setHashTag(post.getHashtag() != null ? convertFromJson(post.getHashtag()) : null);
        dto.setSharedPostId(post.getId());
        dto.setCreatedAt(TimeMapperUtils.formatFacebookTime(post.getCreatedAt()));
        dto.setSharedPostAvatar(users.getAvatar());
        dto.setSharedPostName(users.getFullName());
        dto.setSharedPostType(post.getPostType());
        dto.setLikeCount(postRepository.countReactionsByPostId(post.getId()));
        dto.setCommentCount(postRepository.countCommentsByPostId(post.getId()));
        dto.setSharedCount(postRepository.countSharesByPostId(post.getId()));
        if (mediaFileList != null && !mediaFileList.isEmpty()) {
            dto.setMediaFiles(mediaFileList.stream().map(mediaFile -> {
                MediaDto mediaDto = new MediaDto();
                mediaDto.setMediaType(mediaFile.getMediaType());
                mediaDto.setFileUrl(mediaFile.getUrl());
                mediaDto.setFileSize(mediaFile.getFileSize());
                mediaDto.setFormat(mediaFile.getMediaFormat());
                return mediaDto; }).toList());
        } else {
            dto.setMediaFiles(null);
        }
        return dto;
    }
}
