package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.model.gemini.GeminiRequest;
import com.anhoang.socialnetworkdemo.model.gemini.GeminiResponse;
import com.anhoang.socialnetworkdemo.model.post.PostRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.service.GeminiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
@Slf4j
public class IGeminiService implements GeminiService {
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String url;

    private final RestTemplate restTemplate;

    public IGeminiService(RestTemplate restTemplate,
                          @Value("${gemini.apikey}") String apiKey,
                          @Value("${gemini.api}") String url,
                          ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.url = url;
    }

    @Override
    public ResponseBody<?> chatWithGeminiText(String message) {
        try {
            //String jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + message + "\"}]}]}";
            GeminiRequest req = new GeminiRequest();
            req.setGeminiData(message, null, null);
            String jsonString = objectMapper.writeValueAsString(req);
            System.out.println(jsonString);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            // Gọi API POST với body
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                return new ResponseBody<>(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }

    @Override
    public ResponseBody<?> chatWithGeminiImage(String message, MultipartFile file) {
        try {
            String jsonString;
            if (file != null) {
                if (message == null) message = "Mô tả ảnh";
                byte[] fileBytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                jsonString = "{ \"contents\": [{ \"parts\": [ {\"text\": \"" + message + "\"}, " +
                        "{\"inline_data\": {\"mime_type\": \"" + file.getContentType() + "\", \"data\": \"" + base64Image + "\"}}]}]}";
            } else {
                jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + message + "\"}]}]}";
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                return new ResponseBody<>(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }

    @Override
    public ResponseBody<?> aiCheckPostCreate(PostRequest req) {
        try {
            String json = objectMapper.writeValueAsString(req);
            String text = "Bạn là một AI chuyên kiểm tra nội dung dựa trên tiêu chuẩn cộng đồng của một nền tảng mạng xã hội. Với dữ liệu của bài đăng sau, hãy thực hiện các bước:\n" +
                    "\n" +
                    "Kiểm tra nội dung (content), thẻ hashtag (hashTag), vị trí (location), và vai trò của người đăng bài (postRole).\n" +
                    "Kiểm tra xem bài viết có vi phạm các tiêu chuẩn cộng đồng sau đây hay không:\n" +
                    "Nội dung có chứa ngôn từ gây thù hận, tục tĩu, bạo lực hoặc phân biệt đối xử.\n" +
                    "Hashtag có chứa từ khóa phản cảm, không phù hợp hoặc vi phạm quy định.\n" +
                    "Bài viết có thông tin sai lệch hoặc nội dung gây hiểu lầm.\n" +
                    "Kết luận:\n" +
                    "Nếu bài viết vi phạm tiêu chuẩn cộng đồng, chỉ trả về 1 chữ \"true\".\n" +
                    "Nếu không vi phạm, trả về chỉ 1 chữ \"false\".\n" +
                    "Nội dung bài đăng là : " + json;
            return chatWithGeminiTextWithPromptText(text, "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody<?> chatWithGeminiTextWithPromptText(String promptText, String message) {
        try {
            String result;
            if (promptText != null) {
                result = promptText + message;
            } else {
                result = message;
            }
            GeminiRequest req = new GeminiRequest();
            req.setGeminiData(result, null, null);
            String jsonString = objectMapper.writeValueAsString(req);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            // Goi API POST voi body
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                //Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                GeminiResponse response1 = objectMapper.readValue(response.getBody(), GeminiResponse.class);
                return new ResponseBody<>(
                        response1.getCandidates().get(0)
                                .getContent().getParts().get(0).getText(),
                        ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Request gemini not found");
            e.printStackTrace();
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }
}
