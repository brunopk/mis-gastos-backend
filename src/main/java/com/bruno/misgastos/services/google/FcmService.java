package com.bruno.misgastos.services.google;

import com.bruno.misgastos.exceptions.ApiException;

/**
 * <p>Service for interacting with Firebase Cloud Messaging (FCM).</p>
 * <p>Used mainly for sending push notifications.</p>
 */
public interface FcmService {

  /**
   * Send push notifications using FCM
   * @param deviceToken Obtained on the frontend side after subscription
   * @param title Title for the push notification.
   * @param body Body for the push notification.
   * @throws ApiException Throws this exception if some problem occurs trying to send the notification.
   */
  void sendPushNotification(String deviceToken, String title, String body) throws ApiException;
}
