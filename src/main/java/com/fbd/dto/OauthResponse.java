package com.fbd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OauthResponse implements Serializable {
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
