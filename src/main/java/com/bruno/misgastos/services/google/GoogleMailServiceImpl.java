package com.bruno.misgastos.services.google;

import com.bruno.misgastos.entities.GoogleAuthToken;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.exceptions.UnauthorizedException;
import com.bruno.misgastos.respositories.GoogleAuthTokenSpringDataRepository;
import com.bruno.misgastos.utils.ErrorMessages;
import com.bruno.misgastos.utils.GoogleUtils;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class GoogleMailServiceImpl implements GoogleMailService {

  private static final JsonFactory JSON_FACTORY = new GsonFactory();

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private final GoogleAuthTokenSpringDataRepository googleAuthTokenRepository;

  @Autowired
  public GoogleMailServiceImpl(GoogleAuthTokenSpringDataRepository googleAuthTokenRepository) {
    this.googleAuthTokenRepository = googleAuthTokenRepository;
  }

  @Override
  public void sendMail(String toEmailAddress, String subject, String htmlBody) {
    try {
      // Based in https://developers.google.com/workspace/gmail/api/guides/sending
      // TODO: Investigate how to obtain tokens from already logged users. Try to use Spring libraries for Google.
      Credential credential = getUserCredentials(googleAuthTokenRepository);
      Gmail service = new Gmail.Builder(HTTP_TRANSPORT,
        JSON_FACTORY,
        credential)
        .build();

      Properties props = new Properties();
      Session session = Session.getDefaultInstance(props, null);
      MimeMessage email = new MimeMessage(session);
      email.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
      email.setSubject(subject);
      email.setContent(htmlBody, new MediaType("text", "html", StandardCharsets.UTF_8).toString());

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      email.writeTo(buffer);
      byte[] rawMessageBytes = buffer.toByteArray();
      String encodedEmail = Base64.getEncoder().encodeToString(rawMessageBytes);
      com.google.api.services.gmail.model.Message gmailMessage =
          new com.google.api.services.gmail.model.Message();
      gmailMessage.setRaw(encodedEmail);


      service.users().messages().send("me", gmailMessage).execute();
    } catch (MessagingException | IOException ex) {
      throw new ApiException(ex);
    }
  }

  private Credential getUserCredentials(
    GoogleAuthTokenSpringDataRepository googleAuthTokenRepository) {
    GoogleAuthToken token =
      googleAuthTokenRepository
        .getLastActiveToken()
        .orElseThrow(
          () ->
            new UnauthorizedException(
              ErrorCode.UNAUTHORIZED, ErrorMessages.NO_VALID_TOKEN_FOUND));
    return GoogleUtils.getUserCredentials(token, null);
  }
}
