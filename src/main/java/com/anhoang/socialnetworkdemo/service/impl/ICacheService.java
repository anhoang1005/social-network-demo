package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.model.cache.UserCacheDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class ICacheService {
    private final Map<String, UserCacheDto> userCacheMap = new ConcurrentHashMap<>();
}
