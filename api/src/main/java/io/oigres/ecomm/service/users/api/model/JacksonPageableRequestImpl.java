package io.oigres.ecomm.service.users.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JacksonPageableRequestImpl extends PageableRequestImpl {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public JacksonPageableRequestImpl(
            @JsonProperty("page") int page,
            @JsonProperty("size") int size) {
        super(page, size, SortRequest.unsorted());
    }

}
