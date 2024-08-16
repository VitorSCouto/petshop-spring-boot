package dev.vitor.petshop.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRequest {

    private Long userId;

    private String street;

    private String city;

    private String neighborhood;

    private String complement;
    private String tag;
}
