package com.visionary.gmailapi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    EditText to, message, subject;
    Button sendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        to = findViewById(R.id.To);
        message = findViewById(R.id.Message);
        subject = findViewById(R.id.Subject);
        sendMail = findViewById(R.id.send_btn);

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMailNew sendMailNew = new SendMailNew("abc@gmail", to.getText().toString(),
                        subject.getText().toString(), message.getText().toString());
                sendMailNew.execute();
            }
        });

    }

    public static class SendMailNew extends AsyncTask {

        private static final String APPLICATION_NAME = "GMAIL API";
        private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        static String clientId = "", clientSecret = "", access_token = "", refresh_token = "";
        private String From = "";
        private String To = "";
        private String subject = "";
        private String message = "";
        private Gmail service;
        private MimeMessage mail;
        private GoogleCredential credential;

        public SendMailNew(String From, String TO, String subject,
                String message) {
            this.From = From;
            this.To = TO;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            credential = authorize();
            service = createGmail();
            try {
                mail = createEmail();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                sendMessage(service, From, mail);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public MimeMessage createEmail() throws MessagingException {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);

            email.setFrom(new InternetAddress(From));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(To));
            email.setSubject(subject);
            email.setText(message);

            return email;
        }

        public Message createMessageWithEmail(MimeMessage emailContent)
                throws MessagingException, IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            emailContent.writeTo(buffer);
            byte[] bytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
            Message message = new Message();
            message.setRaw(encodedEmail);
            return message;
        }

        public Message sendMessage(Gmail service, String userId, MimeMessage emailContent)
                throws MessagingException, IOException {
            Message message = createMessageWithEmail(emailContent);
            message = service.users().messages().send(userId, message).execute();

            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message;
        }

        private GoogleCredential authorize() {
            return new GoogleCredential.Builder()
                    .setTransport(AndroidHttp.newCompatibleTransport())
                    .setJsonFactory(JSON_FACTORY)
                    .setClientSecrets(clientId, clientSecret)
                    .build()
                    .setAccessToken(access_token)
                    .setRefreshToken(refresh_token);
        }

        private Gmail createGmail() {
            //Here GoogleNetHttpTransport.newTrustedTransport() does not works as per documentation
            return new Gmail.Builder(AndroidHttp.newCompatibleTransport(), JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
    }
}