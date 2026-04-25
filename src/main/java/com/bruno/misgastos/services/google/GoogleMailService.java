package com.bruno.misgastos.services.google;

public interface GoogleMailService {

  /**
   * Sends a mail using the account of the current user.
   * @param toEmailAddress To which email address send the email
   * @param subject Subject of the mail
   * @param htmlBody HTML code for the body of the mail
   */
  void sendMail(String toEmailAddress, String subject, String htmlBody);
}
