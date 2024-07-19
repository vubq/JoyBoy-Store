package com.vubq.joyboystore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSATCDto {

    private String fullName;
    private String phoneNumber;
    private String address;
    private String voucherId;
    private Double totalAmount;
    private Double totalAmountNet;
    private String noteByAdmin;
    private String noteByCustomer;
    private Double moneyPaid;
    private Double moneyRefunds;
    private Boolean isVoucher;
    List<OrderDetailSATCDto> listOrderDetail;
}
