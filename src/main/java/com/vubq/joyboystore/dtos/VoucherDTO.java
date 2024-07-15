package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.enums.EVoucherType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDTO {

    private String id;
    private String code;
    private EVoucherType type;
    private Double value;
    private Date startDate;
    private Date endDate;
    private String createdBy;
    private Date createdAt;
    private EStatus status;
    private Integer quantity;
}
