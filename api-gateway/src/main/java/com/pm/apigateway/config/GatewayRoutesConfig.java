package com.pm.apigateway.config;

import com.pm.apigateway.filter.JwtValidationGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {


    private JwtValidationGatewayFilterFactory jwtValidationGatewayFilterFactory;

    public GatewayRoutesConfig(JwtValidationGatewayFilterFactory jwtValidationGatewayFilterFactory) {
        this.jwtValidationGatewayFilterFactory = jwtValidationGatewayFilterFactory;

    }

    @Bean
    public RouteLocator patientRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patient-service-route", r -> r
                        .path("/api/patients", "/api/patients/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtValidationGatewayFilterFactory.apply(new Object()))
                        )
                        .uri("http://patient-service:4000"))
                .route("api-docs-patient-route", r -> r
                        .path("/api-docs/patients")
                        .filters(f -> f
                                .rewritePath("/api-docs/patients", "/v3/api-docs")
                        )
                        .uri("http://patient-service:4000"))
                .route("api-docs-auth-route", r -> r
                        .path("/api-docs/auth")
                        .filters(f -> f
                                .rewritePath("/api-docs/auth", "/v3/api-docs")
                        )
                        .uri("http://auth-service:4005"))
                .route("auth-service-route", r->r
                        .path("/auth/**")
                        .filters(f->f
                                .stripPrefix(1)
                                //.filter(jwtValidationGatewayFilterFactory.apply(new Object()))
                        )
                        .uri("http://auth-service:4005"))
                .build();
    }
}







