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
