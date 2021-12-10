package tfg.urjc.mydoiinfo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/conference").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/jcr").hasRole("ADMIN");

        //Other endpoints are public
        http.authorizeRequests().anyRequest().permitAll();

        http.csrf().disable();

        //Use Basic Auth
        http.httpBasic();

        //Disable logout redirection
        http.logout().logoutSuccessHandler((rq, rs, a) -> {	});
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }
}
