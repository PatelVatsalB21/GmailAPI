# GmailAPI
Sending emails with Gmail API

This repository is demonstration of using Gmail API of Google

# Important Notice
`GoogleNetHttpTransport.newTrustedTransport()` does not works as per documentation instead use `AndroidHttp.newCompatibleTransport()` to avoid General Security exception.

Following things are required beforehand :-

- Client ID and Client Secret
- Access Token
- Refresh Token

# Working
In this repo Async class `SendMailNew` is responsible for everything related to Gmail API. Firstly new Google Credentials are made with `authorize()`. Then after a service(Gmail) is made with `createGmail()`. And then MimeMessage(Mail) is created with `createEmail()` and encodes with `createMessageWithEmail()`.

# Additional Information
You can find help for Client ID, Client Secret, Access Token & Refresh Token from [here](https://github.com/PatelVatsalB21/GoogleAPITokens).

Here Javamail libraries are also used as [activation.jar](https://github.com/PatelVatsalB21/GmailAPI/blob/3096ec49825c68a413640170abb152852148c63b/app/libs/activation.jar), [additional.jar](https://github.com/PatelVatsalB21/GmailAPI/blob/3096ec49825c68a413640170abb152852148c63b/app/libs/additional.jar), [mail.jar](https://github.com/PatelVatsalB21/GmailAPI/blob/3096ec49825c68a413640170abb152852148c63b/app/libs/mail.jar) which can be found in **libs** of project.

Detailed documentation can be found [here](https://developers.google.com/gmail/api).

# Dependencies
` 
implementation('com.google.api-client:google-api-client-android:1.22.0') {
        exclude group: 'com.google.thirdparty'
        exclude group: 'com.google.guava'
    }
    `
    
`
implementation('com.google.apis:google-api-services-gmail:v1-rev44-1.22.0') {
        exclude group: 'com.google.thirdparty'
        exclude group: 'com.google.guava'
    }
    `
