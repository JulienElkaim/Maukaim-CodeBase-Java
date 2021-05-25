package com.maukaim.org.utils.oauth2.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Oauth2ClientCredentialProperties {
    private String accessTokenUri;
    private String clientId;
    private String clientSecret;
    private List<String> scopes = new ArrayList<>();

}
