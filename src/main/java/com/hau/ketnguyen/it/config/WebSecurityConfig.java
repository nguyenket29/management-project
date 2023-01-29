package com.hau.ketnguyen.it.config;

import com.google.common.collect.ImmutableList;
import com.hau.ketnguyen.it.common.util.JwtTokenUtil;
import com.hau.ketnguyen.it.config.auth.Commons;
import com.hau.ketnguyen.it.config.auth.JWTAuthenticationFilter;
import com.hau.ketnguyen.it.config.auth.JWTAuthorizationFilter;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.service.RefreshTokenService;
import com.hau.ketnguyen.it.service.UserService;
import com.hau.ketnguyen.it.service.impl.auth.AuthServiceimpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenUtil tokenUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final EntryPointAuthenticationConfig entryPointConfig;
    private final AuthServiceimpl authServiceimpl;
    private final UserReps userReps;
    private final UserInfoReps userInfoReps;

    public WebSecurityConfig(JwtTokenUtil tokenUtil, RefreshTokenService refreshTokenService, UserService userService,
                             EntryPointAuthenticationConfig entryPointConfig, AuthServiceimpl authServiceimpl,
                             UserReps userReps, UserInfoReps userInfoReps) {
        this.tokenUtil = tokenUtil;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.entryPointConfig = entryPointConfig;
        this.authServiceimpl = authServiceimpl;
        this.userReps = userReps;
        this.userInfoReps = userInfoReps;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and().authorizeRequests()
                .antMatchers(Commons.PUBLIC_URLs).permitAll()
                .anyRequest().authenticated()
                .and().logout()
                .and().csrf().disable()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(),
                        bCryptPasswordEncoder(), tokenUtil, refreshTokenService, authServiceimpl, userInfoReps, userReps))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic().authenticationEntryPoint(entryPointConfig);

        http.headers().frameOptions().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("*"));
        configuration.setExposedHeaders(ImmutableList.of("Authorization"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/actuator/**");
    }
}
