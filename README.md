This is a simple demo app that shows how to set up secure login using Spring Boot 3.3.5, Java 17, and OAuth2. It integrates third-party authentication providers to make logging in easy and secure.


Note: Starting from Spring Boot 3.2.2, the default in-memory user store is disabled if you’re using OAuth2, SAML2, 
or resource server dependencies, unless you explicitly set `spring.security.user.name` and `spring.security.user.password`. 
If you still need it, you can also define the user store as a bean. More at:
https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.2-Release-Notes#auto-configured-user-details-service
