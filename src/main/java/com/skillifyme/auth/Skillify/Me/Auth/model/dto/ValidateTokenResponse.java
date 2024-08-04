package com.skillifyme.auth.Skillify.Me.Auth.model.dto;

import lombok.Data;

@Data
public class ValidateTokenResponse {
    String email;
    Boolean valid;
}
