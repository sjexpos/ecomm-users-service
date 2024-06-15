package io.oigres.ecomm.service.users.usecases.users.images.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProfileImageDTO {
    private Long id;
    private String imageURL;
    private String status;
    private String type;
}
