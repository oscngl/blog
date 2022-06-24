package com.osc.blog.security.config;

import com.osc.blog.business.abstracts.UserService;
import com.osc.blog.security.entrypoint.fail.AuthenticationFailureEntryPoint;
import com.osc.blog.security.filter.CustomAuthenticationFilter;
import com.osc.blog.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationFailureEntryPoint failureEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(failureEntryPoint);

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v**/auth/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v**/auth/**").permitAll();

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v**/tokens/**").permitAll();

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v**/articles/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v**/articles/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v**/articles/**").hasAnyAuthority("ROLE_USER" , "ROLE_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v**/roles/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v**/roles/**").hasAnyAuthority("ROLE_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v**/topics/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v**/topics/**").hasAnyAuthority("ROLE_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v**/users/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v**/users/**").hasAnyAuthority("ROLE_USER");

        http.authorizeRequests().anyRequest().authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
