package io.oigres.ecomm.service.users.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonErrorHandlerResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    
}
