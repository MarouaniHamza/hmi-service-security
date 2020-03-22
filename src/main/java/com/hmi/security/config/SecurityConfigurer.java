package com.hmi.security.config;

import com.hmi.security.config.auth.*;
import com.hmi.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collections;

@EnableWebSecurity
@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Autowired
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    public CustomAuthenticationProvider authenticationProvider;

    @Autowired
    public CustomSuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll().anyRequest()
                .authenticated().and().exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterAfter(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }

    @Bean(name = "tokenAuthenticationManager")
    public AuthenticationManager tokenAuthenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    public AuthenticationManager daoAuthenticationManager() {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider()));
    }

    @Bean
    public CustomAuthenticationTokenFilter authenticationTokenFilter() throws Exception {

        RequestMatcher requestMatcher = new NegatedRequestMatcher(new RegexRequestMatcher("/auth/token", HttpMethod.POST.name()));
        CustomAuthenticationTokenFilter authenticationTokenFilter = new CustomAuthenticationTokenFilter(requestMatcher);
        authenticationTokenFilter.setAuthenticationManager(tokenAuthenticationManager());
        authenticationTokenFilter.setAuthenticationSuccessHandler(successHandler);
        return authenticationTokenFilter;

    }

    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setAuthenticationManager(daoAuthenticationManager());
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        return usernamePasswordAuthenticationFilter;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
