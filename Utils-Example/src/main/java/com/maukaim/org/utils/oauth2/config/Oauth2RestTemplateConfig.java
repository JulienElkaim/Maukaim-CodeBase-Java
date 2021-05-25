package com.maukaim.org.utils.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.util.Collections;


@Configuration
public class Oauth2RestTemplateConfig {


    /**
     * Must define the 4 parameters of Oauth2ClientCredentialsProperties into application-{profile}.yml
     * @return Properties required to set up a basic Oauth2RestTemplate caller
     */
    @Bean
    @ConfigurationProperties("oauth2.credentials.example-api")
    @Qualifier("simpleClientProperties")
    Oauth2ClientCredentialProperties simpleClientProperties(){return new Oauth2ClientCredentialProperties();}

    @Autowired
    @Qualifier("simpleClientProperties")
    private Oauth2ClientCredentialProperties clientCredentialProperties;

    /**
     *  How to use ? -> Simply autowire this bean into your Connector and use it to call .post .get, .exchange, aso....
     * @return Rest Template able to provide Oauth2 authentication by calling the token provider we... provided.
     */
    @Bean
    @Qualifier("exampleRestTemplate")
    public OAuth2RestTemplate oauth2RestTemplate(){

        ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
        clientCredentialsResourceDetails.setAccessTokenUri(this.clientCredentialProperties.getAccessTokenUri());
        clientCredentialsResourceDetails.setClientId(this.clientCredentialProperties.getClientId());
        clientCredentialsResourceDetails.setClientSecret(this.clientCredentialProperties.getClientSecret());
        clientCredentialsResourceDetails.setScope(this.clientCredentialProperties.getScopes());


        OAuth2RestTemplate restTemplate =  new OAuth2RestTemplate(clientCredentialsResourceDetails);
        AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(
                Collections.singletonList(new ClientCredentialsAccessTokenProvider())
        );
        restTemplate.setAccessTokenProvider(accessTokenProvider);

        return restTemplate;
    }
}
