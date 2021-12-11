package tfg.urjc.mydoiinfo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Order(1)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserAuthenticationProvider userAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //This security configuration only applies for the API REST
        http.antMatcher("/api/**");

        //URLs that are only accessible by the admin
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/conference").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/jcr").hasAuthority("ADMIN");

        //Other endpoints are public
        http.authorizeRequests().anyRequest().permitAll();

        http.csrf().disable();

        //Use Basic Auth
        http.httpBasic();

        //Allow CORS for the API endpoints
        http.cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }

    /*
    Configuration necessary for the application to receive requests from different origins (hosts) without these
    requests being blocked by the server.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        //Allow all hosts since the API is public
        configuration.setAllowedOrigins(Arrays.asList("*"));
        //The only allowed methods are GET and POST
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        configuration.setAllowCredentials(true);
        //Establish the allowed headers
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //It is established that this configuration should only be applied for the API
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
