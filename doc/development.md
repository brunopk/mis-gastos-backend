# Development

## Spring properties

It's recommended to install [Spring Boot Assistant](https://plugins.jetbrains.com/plugin/17747-spring-boot-assistant) plugin on Intellij IDEA for YAML autocompletion.

## Push notifications

To test push notifications : 

1. If not done before, create the service account as explained in the [Push notifications with Firebase](/doc/google.md#push-notifications-with-firebase) section from [`/doc/google.md`](/doc/google.md).
2. Create the **Web** application for the frontend through **Project Settings** > **General** > **Your apps** > **Add app**, type **Web** (**Your apps** is at the bottom) and copy`firebaseConfig` constant from the JavaScript snippet provided by Google in **Your apps** section.
3. Generate and copy the VAPID (Private key) in **Project Settings** > **Cloud Messaging** > **Web Push certificates** > **Generate key pair** (**Cloud Messaging** is a tab).
4. Create `src/main/resources/static/push-test.html` : 
      ```html 
      <!DOCTYPE html>
      <html lang="en">
      <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>FCM Push Test</title>
      </head>
      <body>
          <h1>Firebase Push Notification Test</h1>
          <button id="generateTokenBtn">
              Generate Device Token
          </button>
          <pre id="token">No token generated yet.</pre>
          <script type="module">
   
              import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-app.js";
   
              import {
                getMessaging,
                getToken
              } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-messaging.js";
   
              const firebaseConfig = {};
   
              const vapidKey = "xxx";
              const app = initializeApp(firebaseConfig);
              const messaging = getMessaging(app);
   
              const tokenDiv = document.getElementById("token");
              const button = document.getElementById("generateTokenBtn");
              button.addEventListener("click", async () => {
   
                try {
                  const permission =
                    await Notification.requestPermission();
   
                  if (permission !== "granted") {
                    tokenDiv.innerText = "Notification permission denied.";
                    return;
                  }
   
                  const token = await getToken(messaging, {
                    vapidKey: vapidKey
                  });
   
                  if (!token) {
                    tokenDiv.innerText = "No token generated.";
                    return;
                  }
   
                  tokenDiv.innerText = token;
   
                } catch (error) {
                  console.error(error);
                  tokenDiv.innerText = "Error:\n\n" + error.message;
                }
              });
          </script>
      </body>
      </html>
      ```
   
      Important:
      - `static` folder must exist in order for Spring to automatically server the JavaScript file.
      - Define `firebaseConfig` constant with the value obtained in step 2.
      - Replace `xxx` with the value obtained in step 3.
5. Create the service worker into `src/main/resources/static/firebase-messaging-sw.js` : 
      ```js 
      // firebase-messaging-sw.js
   
      importScripts('https://www.gstatic.com/firebasejs/10.12.2/firebase-app-compat.js');
      importScripts('https://www.gstatic.com/firebasejs/10.12.2/firebase-messaging-compat.js');
   
      firebase.initializeApp({
        apiKey: "YOUR_API_KEY",
        authDomain: "YOUR_PROJECT.firebaseapp.com",
        projectId: "YOUR_PROJECT_ID",
        storageBucket: "YOUR_PROJECT.appspot.com",
        messagingSenderId: "YOUR_SENDER_ID",
        appId: "YOUR_APP_ID"
      });
   
      const messaging = firebase.messaging();
   
      messaging.onBackgroundMessage((payload) => {
        console.log('[firebase-messaging-sw.js] Background message received:', payload);
      });
      ```
   
      Important: create the service worker file exactly as `firebase-messaging-sw.js` otherwise Firebase won't use it.
6. Open the `push-test.html` through http://localhost:8080/push-test.html (replace *localhost* for the corresponding hostname) and click **Generate Device Token**.
7. Modify some Java file, for example [`RecurrentSpendTaskRunnerImpl.java`](/src/main/java/com/bruno/misgastos/tasks/RecurrentSpendTaskRunnerImpl.java) to invoke the `sendPushNotification` method from [`FcmService.java`](/src/main/java/com/bruno/misgastos/services/google/FcmService.java) :
      ```java
      private void processAutomaticTask(Task task) {
          TaskConfig taskConfig = task.getTaskConfig();
          Spend spend = buildSpend(task);
          spendRepository.save(spend);
   
          // Just for testing
          TaskDto googleTask = buildGoogleTasksTask(task);
          fcmService.sendPushNotification("xxx", "Test notification", "Hello world!");
          // ***
   
          LOGGER.info("Spend created: {} (task_config_name={}, task_id={})", spend, taskConfig.getTaskName(), task.getId());
        }
      ```
   
      Replace `xxx` with the token obtained in step 6.

## Links

- [Sending Push Notifications Using Spring Boot and Firebase](https://medium.com/@AlexanderObregon/sending-push-notifications-using-spring-boot-and-firebase-e1227a7eea99)
- [Subscribe a user to push notifications](https://web.dev/articles/push-notifications-subscribing-a-user)
- [Push notifications overview](https://web.dev/articles/push-notifications-overview)