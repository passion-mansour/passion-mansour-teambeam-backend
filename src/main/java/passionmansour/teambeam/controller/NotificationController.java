package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.notification.CreateNotificationRequest;
import passionmansour.teambeam.model.dto.notification.NotificationDto;
import passionmansour.teambeam.model.dto.notification.NotificationListResponse;
import passionmansour.teambeam.service.NotificationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notification Controller", description = "알림 관련 API입니다.")
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/notification/{projectId}")
    public ResponseEntity<?> createNotification(@RequestHeader("Authorization") String token,
                                                @PathVariable("projectId") Long projectId,
                                                @RequestBody CreateNotificationRequest request) {

        notificationService.saveNotification(token, projectId, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "successfully create notification");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/notification")
    public ResponseEntity<NotificationListResponse> getNotificationList(@RequestHeader("Authorization") String token) {
        List<NotificationDto> list = notificationService.getList(token);

        NotificationListResponse response = new NotificationListResponse();
        response.setMessage("Successfully retrieved list");
        response.setNotificationCount(list.size());
        response.setNotificationList(list);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/notification/{notificationId}")
    public ResponseEntity<NotificationListResponse> updateNotification(@RequestHeader("Authorization") String token,
                                                @PathVariable("notificationId") Long notificationId) {
        List<NotificationDto> notificationList = notificationService.updateNotification(token, notificationId);

        NotificationListResponse response = new NotificationListResponse();
        response.setMessage("Successfully update notification");
        response.setNotificationList(notificationList);
        response.setNotificationCount(notificationList.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/notification/{notificationId}")
    public ResponseEntity<NotificationListResponse> deleteNotification(@RequestHeader("Authorization") String token,
                                                                       @PathVariable("notificationId") Long notificationId) {
        List<NotificationDto> notificationList = notificationService.deleteNotification(token, notificationId);

        NotificationListResponse response = new NotificationListResponse();
        response.setMessage("Successfully delete notification");
        response.setNotificationList(notificationList);
        response.setNotificationCount(notificationList.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
