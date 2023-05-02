# How OAuth/Social login works with Spring Boot 3.0
1. **Resource Owner**: This is the user or system that owns the protected resources and can grant access to them.
2. **Client**: This is the system (app, browser etc.) that requires access to the protected resources or to access those resources the client must have the appropriate access token.
3. **Authorization Server**: This server receives requests from the client for access token and issues them upon successful authentication and consent by the resource owner. The authorization server exposes two (2) endpoints which are:
   - The authorization endpoint which handles the interactive authentication and consent of the user.
   - The token endpoint which is involved in machine to machine interaction.
4. **Resource Server**: This is the backend of our application which has the protected resources a resource owner and a client wants to access.

## Steps involved in OAuth 2.0
- The user makes a request on the client application.
- The client application redirects to the authorisation server (Google, Github, Okta, Facebook, etc).
- The authorization server asks the resource owner for authentication. If the user is not signed in or doesn't have an access token, they then provide their credentials.
- It then asks the user to authorize the client application, then it redirects to the client application with the authorization code.
- The client application then receives the authorization code after the redirect in the previous step.
- It then transmits the client credentials and the authentication code it received to the Authorization server.
- The authorization server then returns a valid access token.
- The end user can then access secured endpoints from the resource server in our application via access token sent in the response header from the authorization server, and gets a response.
