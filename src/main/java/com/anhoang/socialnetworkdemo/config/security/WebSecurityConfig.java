package com.anhoang.socialnetworkdemo.config.security;

import com.anhoang.socialnetworkdemo.config.security.user_detail.CustomUserDetailService;
import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.exceptions.security.CustomAccessDeniedHandler;
import com.anhoang.socialnetworkdemo.exceptions.security.CustomAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {
	private final CustomUserDetailService customUserDetailService;
	private final CustomFilterJwt customFilterJwt;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		http.csrf(AbstractHttpConfigurer::disable);
		http.userDetailsService(customUserDetailService);
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.exceptionHandling(exceptionHandling ->
				exceptionHandling
						.authenticationEntryPoint(customAuthenticationEntryPoint)
						.accessDeniedHandler(customAccessDeniedHandler)
		);
        http.addFilterBefore(customFilterJwt, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(requests -> requests
				.requestMatchers(
						"/js/**",
						"/img/**",
						"/asset/**",
						"/css/**",
						"/vnbook/**",
						"/",
						"/ws",
						"/ws/**",
						"/favicon.ico",
						"/api/guest/**").permitAll()
				.requestMatchers(
						"/api/root/**").hasRole(Roles.BaseRole.SUPER_ADMIN.name())
				.requestMatchers(
						"/api/admin/**").hasAnyRole(
								Roles.BaseRole.ADMIN.name(), Roles.BaseRole.SUPER_ADMIN.name())
				.requestMatchers(
						"/api/user/**").hasAnyRole(Roles.BaseRole.USER.name())
				.requestMatchers(
						"/api/staff/**").hasAnyRole(Roles.BaseRole.MODERATOR.name())
				.anyRequest().authenticated());
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://127.0.0.1:5501", "http://127.0.0.1:3000"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
