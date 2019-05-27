package com.em248.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperties {


    private ApplicationProperties.AdminDatabase adminDatabase = new AdminDatabase();


    private final Security security = new Security();

    private final CorsConfiguration cors = new CorsConfiguration();




    @Data
    public static class AdminDatabase {

        private String username;

        private String password;

        private String host;

        private String port;
    }

    @Data
    public static class Security {
        private final ApplicationProperties.Security.ClientAuthorization clientAuthorization = new ApplicationProperties.Security.ClientAuthorization();
        private ApplicationProperties.Security.Authentication authentication = new ApplicationProperties.Security.Authentication();
        private final ApplicationProperties.Security.RememberMe rememberMe = new ApplicationProperties.Security.RememberMe();

        public ApplicationProperties.Security.ClientAuthorization getClientAuthorization() {
            return this.clientAuthorization;
        }

        public ApplicationProperties.Security.Authentication getAuthentication() {
            return this.authentication;
        }

        public ApplicationProperties.Security.RememberMe getRememberMe() {
            return this.rememberMe;
        }

        public static class RememberMe {
            @NotNull
            private String key;

            public RememberMe() {
                this.key = null;
            }

            public String getKey() {
                return this.key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }

        public static class Authentication {
            private ApplicationProperties.Security.Authentication.Jwt jwt = new ApplicationProperties.Security.Authentication.Jwt();

            public Authentication() {
            }

            public ApplicationProperties.Security.Authentication.Jwt getJwt() {
                return this.jwt;
            }

            public static class Jwt {
                private String secret;
                private String base64Secret;
                private long tokenValidityInSeconds;
                private long tokenValidityInSecondsForRememberMe;

                public Jwt() {
                    this.secret = null;
                    this.base64Secret = null;
                    this.tokenValidityInSeconds = 1800L;
                    this.tokenValidityInSecondsForRememberMe = 2592000L;
                }

                public String getSecret() {
                    return this.secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public String getBase64Secret() {
                    return this.base64Secret;
                }

                public void setBase64Secret(String base64Secret) {
                    this.base64Secret = base64Secret;
                }

                public long getTokenValidityInSeconds() {
                    return this.tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return this.tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }

        public static class ClientAuthorization {
            private String accessTokenUri;
            private String tokenServiceId;
            private String clientId;
            private String clientSecret;

            public ClientAuthorization() {
                this.accessTokenUri = null;
                this.tokenServiceId = null;
                this.clientId = null;
                this.clientSecret = null;
            }

            public String getAccessTokenUri() {
                return this.accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getTokenServiceId() {
                return this.tokenServiceId;
            }

            public void setTokenServiceId(String tokenServiceId) {
                this.tokenServiceId = tokenServiceId;
            }

            public String getClientId() {
                return this.clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return this.clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }
        }
    }
}
