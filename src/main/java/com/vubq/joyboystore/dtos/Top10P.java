package com.vubq.joyboystore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Top10P {

    private String id;
    private String productName;
    private Long quantity;
    private Double totalMoney;
}
