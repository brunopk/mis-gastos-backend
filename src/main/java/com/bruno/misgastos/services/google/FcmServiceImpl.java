package com.bruno.misgastos.services.google;


import com.bruno.misgastos.exceptions.ApiException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FcmServiceImpl implements FcmService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FcmServiceImpl.class);

  @Override
  public void sendPushNotification(String deviceToken, String title, String body) {
    Message message = Message.builder()
      .setToken(deviceToken)
      .setNotification(Notification.builder()
        .setTitle(title)
        .setBody(body)
        .build())
      .build();

    try {
      String response = FirebaseMessaging.getInstance().send(message);
      LOGGER.debug("Push notification sent successfully: {}", response);
    } catch (FirebaseMessagingException ex) {
      throw new ApiException(ex);
    }
  }
}
