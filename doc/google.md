# Google credentials configuration

In order to use Google Tasks and Gmail APIs on behalf of the user (Oauth2 authorization code flow), follow these steps:

1. Go to the [Google Developers Console](https://console.cloud.google.com/) to enable [Google Tasks API](https://console.cloud.google.com/apis/enableflow?apiid=tasks.googleapis.com).
2. Go to the [Google Developers Console](https://console.cloud.google.com/) to enable [Gmail API](https://console.cloud.google.com/apis/enableflow?apiid=gmail.googleapis.com). 
3. Select **Create project**, enter a project name and select **Create**.
4. Enable the following APIs :
      - Gmail API
      - Google Tasks API
5. Navigate to [APIs & Services (left sidebar) > Credentials](https://console.cloud.google.com/apis/credentials).
6. In the left sidebar, select **OAuth consent screen**.
7. It will take you to the Overview page and ask for **Project Configuration**:
    - Complete the App Information:
      - Set the **App name** (the name of the application asking for consent) to anything you want, for example, *Mis gastos*.
      - For a **Support email**, choose your email address from the dropdown menu.
        - Click **Next**.
    - For Audience, select **External** then click **Next**.
    - Under Contact Information, enter your email address (the same as above is fine). Click **Next**.
    - Read the policy and check the box if you agree. Click **Continue**.
    - Click **Create**.
8. In the left sidebar, select **Audience**:
    - Under **Publishing status > Testing**, select **Publish app**.
        
       > Otherwise, your credentials will expire every 7 days.
9. In the left sidebar, select **Clients**:
    - Click **+ Create Client**.
    - For Application type, choose **Web Application** and give this client ID a name (like “Mis gastos Client”).
    - Add **http://localhost:8080/login/oauth2/code/google** to **Authorized redirect URIs** then select **Create** (**only valid for development**).
      
       > - Note: This is not a placeholder. It is the URI that must be used.
       > - This URL **must** match with URL defined in **`spring.security.oauth2.client.registration.google.redirect-uri`** in properties.
    - Click **Create**.
10. Find the client you just created. Under the Actions column, choose **Download OAuth client** (download icon).


<br>

> **Save downloaded credentials (JSON file) in a secure place as it will be used later (refer to [Steps to run Mis Gastos Backend](/README.md#steps-to-run-mis-gastos-backend) and [Configurations](/README.md#configuration) sections within the [Steps to run Mis Gastos Backend](/README.md#running-mis-gastos-backend) section in [`README.md`](/README.md)).**

<br>

## Additional information

- Steps in [Google credentials configuration](#google-credentials-configuration) are based on the [Scenario 2: You do not have credentials set up yet](https://www.home-assistant.io/integrations/google_tasks/#scenario-2-you-do-not-have-credentials-set-up-yet) from the [Home Assistant Google Tasks integration](https://www.home-assistant.io/integrations/google_tasks/).
- Instead of setting **External** for Audience as described in step 8 of [Google credentials configuration](#google-credentials-configuration) section, another option is to set **Internal** and then under **Test users** adding the same Google account that will be used for Google Tasks (Google email address). This will allow the account as a testing user (not for real production environment).

## Links

- [OAuth 2.0 and the Google OAuth Client Library for Java](https://developers.google.com/api-client-library/java/google-oauth-java-client/oauth2)
- [Google Tasks Java Quickstart](https://developers.google.com/workspace/tasks/quickstart/java)
- [Google API Client Libraries for Java](https://developers.google.com/api-client-library/java)

