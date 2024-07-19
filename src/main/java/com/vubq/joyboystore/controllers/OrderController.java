package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.OrderSATCDto;
import com.vubq.joyboystore.entities.Order;
import com.vubq.joyboystore.entities.OrderDetail;
import com.vubq.joyboystore.entities.Voucher;
import com.vubq.joyboystore.enums.*;
import com.vubq.joyboystore.services.OrderDetailService;
import com.vubq.joyboystore.services.OrderService;
import com.vubq.joyboystore.services.ProductDetailService;
import com.vubq.joyboystore.services.VoucherService;
import com.vubq.joyboystore.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("pay-sales-at-the-counter")
    public Response paySalesAtTheCounter(@RequestBody OrderSATCDto dto) {
        Order orderNew = Order.builder()
                .fullName(dto.getFullName())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .totalAmount(dto.getTotalAmount())
                .totalAmountNet(dto.getTotalAmountNet())
                .moneyPaid(dto.getMoneyPaid())
                .moneyRefunds(dto.getMoneyRefunds())
                .noteByAdmin(dto.getNoteByAdmin())
                .noteByCustomer(dto.getNoteByCustomer())
                .createdAt(new Date())
                .createdBy(this.getTheCurrentlyLoggedInUserId())
                .status(EOrderStatus.SUCCESS)
                .type(EOrderType.AT_THE_SHOP)
                .paymentType(EPaymentType.PAYMENT_IN_SHOP)
                .paymentType2(EPaymentType2.CASH)
                .build();

        Voucher voucher = null;
        if (dto.getIsVoucher()) {
            voucher = this.voucherService.getById(dto.getVoucherId());
            if (voucher == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá không tồn tại.");
            } else {
                if (voucher.getQuantity() > 0) {
                    return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá đã hết.");
                }
            }
        }
        orderNew.setVoucherId(voucher.getId());

        Order order = this.orderService.save(orderNew);

        voucher.setQuantity(voucher.getQuantity() - 1);
        this.voucherService.save(voucher);

        List<OrderDetail> listOrderDetail = new ArrayList<>();
        dto.getListOrderDetail().forEach(od -> {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productDetail(this.productDetailService.getById(od.getProductDetailId()))
                    .quantity(od.getQuantity())
                    .totalAmount(od.getTotalAmount())
                    .totalAmountNet(od.getTotalAmountNet())
                    .productDetailPrice(od.getProductDetailPrice())
                    .productDetailPriceNet(od.getProductDetailPriceNet())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .status(EStatus.ACTIVE)
                    .build();
            listOrderDetail.add(orderDetail);
        });
        this.orderDetailService.saveAll(listOrderDetail);

        return Response.build().ok();
    }
}
