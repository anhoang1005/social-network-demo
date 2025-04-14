package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.entity.*;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.exceptions.users.UnauthorizedException;
import com.anhoang.socialnetworkdemo.mapper.MediaFileMapper;
import com.anhoang.socialnetworkdemo.mapper.PostMapper;
import com.anhoang.socialnetworkdemo.model.media.MediaFormat;
import com.anhoang.socialnetworkdemo.model.notify.PostNotifyDto;
import com.anhoang.socialnetworkdemo.model.post.PostDto;
import com.anhoang.socialnetworkdemo.model.post.PostRequest;
import com.anhoang.socialnetworkdemo.payload.PageData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.repository.HashtagRepository;
import com.anhoang.socialnetworkdemo.repository.PostReactionRepository;
import com.anhoang.socialnetworkdemo.repository.PostRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.FileService;
import com.anhoang.socialnetworkdemo.service.GeminiService;
import com.anhoang.socialnetworkdemo.service.NotificationService;
import com.anhoang.socialnetworkdemo.service.PostService;
import com.anhoang.socialnetworkdemo.utils.AuthenticationUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IPostService implements PostService {
    private final AuthenticationUtils authUtils;
    private final UsersRepository usersRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashTagRepository;
    private final PostMapper postMapper;
    private final FileService fileService;
    private final ObjectMapper objectMapper;
    private final PostReactionRepository postReactionRepository;
    private final GeminiService geminiService;
    private final NotificationService notifyService;

    @Override
    @Transactional
    public ResponseBody<?> guestGetPostHome(String hashtag, int pageNumber, int pageSize) {
        try {
            String myUserCode = authUtils.getUserFromAuthentication().getUserCode();
            Hashtag postHashTag;
            if (hashtag != null) {
                postHashTag = hashTagRepository.findHashtagByName(hashtag);
            } else {
                postHashTag = null;
            }
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize,
                    Sort.by(Sort.Order.desc("createdAt")));
            Page<Post> page = postRepository.userGetPostHome(Post.Status.NORMAL, postHashTag, pageable);
            List<PostDto> listPost = page.stream()
                    .map((post) -> postMapper.entityToPostDto(post, myUserCode))
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(listPost)
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Get get post home error! " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public Hashtag userCreateHashTag(String hashtag) {
        try{
            Hashtag postHashTag = new Hashtag();
            postHashTag.setName(hashtag);
            postHashTag = hashTagRepository.save(postHashTag);
            return postHashTag;
        } catch (Exception e){
            log.error("Create post error! Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userCreatePost(PostRequest req, List<MultipartFile> listFile) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users users = usersRepository.findUsersByUserCode(userCode).orElseThrow(() -> new RequestNotFoundException("User not found!"));
            String check = geminiService.aiCheckPostCreate(req).getData().toString();
            if(check.trim().equals("true")){
                return new ResponseBody<>(null, ResponseBody.Status.SUCCESS, "Bài viết của bạn vi phạm tiêu chuẩn cộng đồng!", ResponseBody.Code.FORBIDDEN);
            }
            Post post = new Post();
            post.setUsers(users);
            post.setIsShared(false);
            post.setContent(req.getContent());
            post.setVisibility(req.getVisibility());
            post.setLocation(req.getLocation());
            post.setStatus(Post.Status.NORMAL);

            List<Hashtag> existingHashTags = hashTagRepository.findHashtagByNameIn(req.getHashTag());
            Set<String> existingHashTagNames = existingHashTags.stream()
                    .map(Hashtag::getName)
                    .collect(Collectors.toSet());
            Set<Hashtag> finalHashTagList = new HashSet<>(existingHashTags);
            for (String hashtag : req.getHashTag()) {
                if (!existingHashTagNames.contains(hashtag)) {
                    Hashtag newHashTag = new Hashtag();
                    newHashTag.setName(hashtag);
                    newHashTag.setPosts(null);
                    newHashTag = hashTagRepository.save(newHashTag);
                    finalHashTagList.add(newHashTag);
                }
            }
            List<String> finalHashTagString = finalHashTagList.stream()
                    .map(Hashtag::getName)
                    .collect(Collectors.toList());
            post.setHashtag(postMapper.convertToJson(finalHashTagString));
            post.setHashtags(finalHashTagList);
            post = postRepository.save(post);
            List<MediaFile> listMedia = new ArrayList<>();
            if(listFile!=null && !listFile.isEmpty()) {
                for (MultipartFile file : listFile) {
                    ResponseBody<?> upload = fileService.uploadToCloudinary(file);
                    MediaFile mediaFile = new MediaFile();
                    MediaFormat mediaFormat = MediaFileMapper.getFormatOfFile(file);
                    mediaFile.setUrl(upload.getData().toString());
                    mediaFile.setFileName(file.getOriginalFilename());
                    mediaFile.setMediaFormat(mediaFormat.getFormat());
                    mediaFile.setMediaType(mediaFormat.getMediaType());
                    mediaFile.setAccessLevel(MediaFile.AccessLevel.PUBLIC);
                    mediaFile.setFileSize(file.getSize());
                    mediaFile.setPost(post);
                    mediaFile.setUsersUpdated(users);
                    listMedia.add(mediaFile);
                }
            }
            post.setMediaFiles(listMedia);
            post = postRepository.save(post);
            return new ResponseBody<>(postMapper.entityToPostDto(post, userCode),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Create post error! Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userUpdatePost(PostRequest req, List<MultipartFile> listFile) {
        try {
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Post post = postRepository.findById(req.getId())
                    .orElseThrow(() -> new RequestNotFoundException("Post not found with ID: " + req.getId()));
            if(!post.getUsers().getUserCode().equals(userCode)){
                throw new UnauthorizedException("ERROR");
            }
            post.setContent(req.getContent());
            post.setVisibility(req.getVisibility());
            post.setLocation(req.getLocation());
            List<String> oldHashtagList = objectMapper.readValue(post.getHashtag(),
                    new TypeReference<>() {});
            List<String> newHashtagList = req.getHashTag();
            Set<String> hashTagSet = new HashSet<>(oldHashtagList);
            hashTagSet.addAll(newHashtagList);
            List<Hashtag> existingHashTags = hashTagRepository.findHashtagByNameIn(hashTagSet.stream().toList());
            List<String> existingHashTagsString = existingHashTags.stream()
                    .map(Hashtag::getName).collect(Collectors.toList());
            Set<Hashtag> updatedHashTagList = new HashSet<>(existingHashTags);
            for (String newHashtag : newHashtagList) {
                if (!existingHashTagsString.contains(newHashtag)) {
                    Hashtag postHashTag = new Hashtag();
                    postHashTag.setName(newHashtag);
                    postHashTag = hashTagRepository.save(postHashTag);
                    updatedHashTagList.add(postHashTag);
                }
            }
            List<Hashtag> hashTagsToRemove = updatedHashTagList.stream()
                    .filter(existingTag -> !newHashtagList.contains(existingTag.getName()))
                    .collect(Collectors.toList());
            for (Hashtag hashTag : hashTagsToRemove) {
                updatedHashTagList.remove(hashTag);
            }
            post.setHashtag(postMapper.convertToJson(newHashtagList));
            post.setHashtags(updatedHashTagList);
            List<String> listMedia = new ArrayList<>();
            if(listFile!=null && !listFile.isEmpty()){
                listFile.forEach(multipartFile -> {
                    ResponseBody<?> upload = fileService.uploadToCloudinary(multipartFile);
                    listMedia.add(upload.getData().toString());
                });
            }
            post = postRepository.save(post);
            return new ResponseBody<>(postMapper.entityToPostDto(post, userCode),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Update post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userDeletePost(Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RequestNotFoundException("Post not found with ID: " + postId));
            if(!post.getUsers().getUserCode().equals(authUtils.getUserFromAuthentication().getUserCode())){
                throw new UnauthorizedException("ERROR");
            }
            postRepository.delete(post);
            return new ResponseBody<>("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Delete post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userSharedPost(PostRequest req) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Post originalPost = postRepository.findById(req.getSharedId())
                    .orElseThrow(() -> new RequestNotFoundException("Post not found with ID: " + req.getSharedId()));
            Post sharedPost = new Post();
            sharedPost.setContent(req.getContent());
            sharedPost.setIsShared(true);
            sharedPost.setSharedPost(originalPost);
            Users user = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            sharedPost.setUsers(user);
            sharedPost.setVisibility(req.getVisibility());
            sharedPost.setLocation(req.getLocation());
            sharedPost.setStatus(Post.Status.NORMAL);
            sharedPost = postRepository.save(sharedPost);

            String toUserCode = originalPost.getUsers().getUserCode();
            if(!toUserCode.equals(userCode)){
                PostNotifyDto postNotifyDto = PostNotifyDto.builder()
                        .postId(originalPost.getId()).postContent(originalPost.getContent())
                        .myReaction(null).myComment(null)
                        .actionUserCode(userCode)
                        .actionUsername(user.getFullName())
                        .actionUserAvatar(user.getAvatar())
                        .build();
                notifyService.sendPostNotifyToUser(Notifications.Type.SHARE_POST,
                        originalPost.getUsers().getUserCode(), originalPost.getId(),
                        postNotifyDto, sharedPost.getCreatedAt());
            }

            return new ResponseBody<>(postMapper.entityToPostDto(sharedPost, userCode),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Share post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userGetTheirListPost(String postHashtag, int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("createdAt")));
            Hashtag hashTag = hashTagRepository.findHashtagByName(postHashtag);
            Page<Post> page = postRepository.userGetTheirPostOneHashTag(
                    Post.Status.NORMAL, userId, hashTag, pageable);
            List<PostDto> listPost = page.stream()
                    .map(post -> postMapper.entityToPostDto(post, authUtils.getUserFromAuthentication().getUserCode()))
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(listPost)
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get post error! Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userGetUserPostOther(String userCode, String hashTag, int pageNumber, int pageSize) {
        try{
            String myUserCode = authUtils.getUserFromAuthentication().getUserCode();
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("createdAt")));
            Hashtag postHashTag = hashTagRepository.findHashtagByName(hashTag);
            Page<Post> page = postRepository.userGetUserPostOther(
                    Post.Status.NORMAL, userCode, postHashTag, Post.Visibility.PUBLIC, pageable);
            List<PostDto> listPost = page.stream()
                    .map(post -> postMapper.entityToPostDto(post, myUserCode))
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(listPost)
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> adminActivePost(Long postId, Post.Status status) {
        try{
            Post post = postRepository.findById(postId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            post.setStatus(status);
            postRepository.save(post);
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("admin active post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> useReactionPost(Long postId, PostReaction.Reaction reaction) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users users = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            Post post = postRepository.findById(postId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            PostReaction postReaction = postReactionRepository.findPostReactionByPostIdAndUserCode(userCode, postId);
            if(postReaction!=null){
                if(reaction==PostReaction.Reaction.NONE){
                    postReactionRepository.delete(postReaction);
                    postRepository.updatePostLikeCount(postId, false);
                } else {
                    postReaction.setReaction(reaction);
                    postReaction = postReactionRepository.save(postReaction);
                }
            } else{
                postReaction = new PostReaction();
                postReaction.setPost(post);
                postReaction.setUsers(users);
                postReaction.setReaction(reaction);
                postReaction = postReactionRepository.save(postReaction);
                postRepository.updatePostLikeCount(postId, true);

                String toUserCode = post.getUsers().getUserCode();
                if(!toUserCode.equals(userCode)) {
                    PostNotifyDto postNotifyDto = PostNotifyDto.builder()
                            .postId(post.getId()).postContent(post.getContent())
                            .myReaction(postReaction.getReaction()).myComment(null)
                            .actionUserCode(userCode)
                            .actionUsername(users.getFullName())
                            .actionUserAvatar(users.getAvatar())
                            .build();
                    notifyService.sendPostNotifyToUser(Notifications.Type.REACTION_POST,
                            post.getUsers().getUserCode(), post.getId(),
                            postNotifyDto, postReaction.getUpdatedAt());
                }

            }
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("reaction post error! Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userGetPostDetail(Long postId) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Post post = postRepository.findById(postId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            String postUserCode = post.getUsers().getUserCode();
            if(post.getStatus() != Post.Status.NORMAL){
                throw new RequestNotFoundException("ERROR");
            }
            if(post.getVisibility() == Post.Visibility.PRIVATE && !postUserCode.equals(userCode)){
                throw new RequestNotFoundException("ERROR");
            }
            PostDto postDto = postMapper.entityToPostDto(post, userCode);
            return new ResponseBody<>(postDto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get post detail error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}