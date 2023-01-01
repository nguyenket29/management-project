package com.hau.ketnguyen.it.service.impl.hau;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.entity.hau.Comments;
import com.hau.ketnguyen.it.repository.hau.CommentReps;
import com.hau.ketnguyen.it.service.CommentService;
import com.hau.ketnguyen.it.service.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CommentReps commentReps;

    @Override
    public void sendCommentToSocket() {
        ObjectMapper mapper = new ObjectMapper();
        List<Comments> comments = this.findAll();

        if (comments != null && !comments.isEmpty()) {
            List<Long> userIds = comments.stream().map(Comments::getUserId).distinct().collect(Collectors.toList());
            List<Integer> userIdInts = new ArrayList<>();
            if (!userIds.isEmpty()) {
                userIds.forEach(u -> userIdInts.add(Integer.parseInt(String.valueOf(u))));
            }

            Map<Integer, Comments> mapUserComments = new HashMap<>();
            if (!userIdInts.isEmpty()) {
                userIdInts.forEach(u -> {
                    List<Comments> commentsList = new ArrayList<>();
                    comments.forEach(c -> {
                        if (c.getUserId() != null && Objects.equals(c.getUserId().intValue(), u)) {
                            commentsList.add(c);
                        }
                    });

                    Comments comment = Collections.max(commentsList, Comparator.comparing(Comments::getId));
                    mapUserComments.put(u, comment);
                });

                userIdInts.forEach(u -> {
                   if (!mapUserComments.isEmpty()) {
                       try {
                           log.info("Send Socket Comment {} with messages: {}",
                                   u, mapper.writeValueAsString(mapUserComments.get(u)));
                           simpMessagingTemplate.convertAndSend("/topic/messages/" + u, mapper.writeValueAsString(mapUserComments.get(u)));
                       } catch (JsonProcessingException e) {
                           throw new RuntimeException(e);
                       }
                   }
                });
            }
        }
    }

    private List<Comments> findAll() {
        List<Comments> comments = new ArrayList<>();
        commentReps.findAll().iterator().forEachRemaining(comments::add);
        return comments;
    }
}
