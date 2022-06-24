# spring-security-with-jwt
Based on the spring-security-config-authority base. 
This project is my notes on JWT 

### Create Controller with simple api
- create 1 controller, Then added spring-security dep into pom

### Changing the default Security Configuration
- Since `WebSecurityConfigurerAdapter` deprecated, Create `SecurityConfiguration` using a `SecurityFilterChain` Bean to configure HttpSecurity or a WebSecurityCustomizer Bean to configure WebSecurity instead

### Modifying SecurityConfiguration as per our custom requirements
- using antMatcher("/not-secured").permitAll(): tested with no auth in postman return 200
- using antMatcher("/secured").authenticated(): tested with no auth in postman return 401
- using .antMatchers("/read-write").hasAuthority("WRITE"): require "WRITE" auth to pass or else return 403
- using .antMatchers("/read-only").hasAuthority("READ"): require "READ" auth to pass or else return 403

### Config to use JWT
- First disable csrf because it will be redundant
- By default spring security will auto generate JESSIONID and stored in HTTPSESSION. Using JWT, we can indicate that we will take care JESSIONID by ourself
    `http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)`: tell SpringSecurity not to generate any token
- Expose header for any consumers consume the apis
- Import jwt dependency
- Create JWTTokenGeneratorFilter to generate token when login
  - Generate JWT Token which comprised of 3 part:
  + header: meta data related to token
  + payload: user-related information
  + signature: hash secret used to verify token later on
  - Then set token to AUTHORIZATION header
- Add JWTTokenGeneratorFilter into security config
- Add JWTTokenValidatorFilter to validate token for request access after logging
- Note that we need implement FE also to set token into header and send to BE for validation
### TAKEAWAY
- Token helps not to share the credentials for every request
- Dont mistake token could replace AUTHENTICATION
- we generate token after AUTHENTICATED succesfully and use the user info in authentication to generate TOKEN, then pass token in further accesses