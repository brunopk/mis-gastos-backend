# Google credentials configuration

In order to use Google Tasks API, OAuth2 credentials for authorization code flow must be configured. One way to this is by following these steps based on the [steps to configure Home Assistant Google Tasks integration](https://www.home-assistant.io/integrations/google_tasks/):


1. First, go to the [Google Developers Console](https://console.cloud.google.com/) to enable [Google Tasks API](https://console.cloud.google.com/apis/enableflow?apiid=tasks.googleapis.com).
2. Select **Create project**, enter a project name and select **Create**.
3. **Enable** the Google Tasks API.
4. Navigate to APIs & Services (left sidebar) > [Credentials](https://console.cloud.google.com/apis/credentials).
5. In the left sidebar, select **OAuth consent screen**.
6. It will take you to the Overview page and ask for **Project Configuration**:
    - Complete the App Information:
      - Set the **App name** (the name of the application asking for consent) to anything you want, for example, *Mis gastos*.
      - For a **Support email**, choose your email address from the dropdown menu.
        - Click **Next**.
    - For Audience, select **External** then click **Next**.
    - Under Contact Information, enter your email address (the same as above is fine). Click **Next**.
    - Read the policy and check the box if you agree. Click **Continue**.
    - Click **Create**.
7. In the left sidebar, select **Audience**:
    - Under **Publishing status > Testing**, select **Publish app**.
        > Otherwise, your credentials will expire every 7 days.
8. In the left sidebar, select **Clients**:
    - Click **+ Create Client**.
    - For Application type, choose **Web Application** and give this client ID a name (like “Mis gastos Client”).
    - Add http://localhost:8081/redirect/oauth to **Authorized redirect URIs** then select **Create** (**only valid for development**).
      > Note: This is not a placeholder. It is the URI that must be used.
    - Click **Create**.
9. Find the client you just created. Under the Actions column, choose **Download OAuth client** (download icon), save this as `client_secret.json` into `src/main/resources`.
    > Do not commit `client_secret.json`


Instead of setting **External** for Audience as described in step 6, another option is to set **Internal** and then under **Test users** adding the same Google account that will be used for Google Tasks (Google email address). This will allow the account as a testing user (not for real production environment).

## Links

- [Steps to configure Home Assistant Google Tasks integration](https://www.home-assistant.io/integrations/google_tasks/)
- [OAuth 2.0 and the Google OAuth Client Library for Java](https://developers.google.com/api-client-library/java/google-oauth-java-client/oauth2)
- [Google Tasks Java Quickstart](https://developers.google.com/workspace/tasks/quickstart/java)
- [Google API Client Libraries for Java](https://developers.google.com/api-client-library/java)

