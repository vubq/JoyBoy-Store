package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.OrderSATCDto;
import com.vubq.joyboystore.entities.Order;
import com.vubq.joyboystore.entities.OrderDetail;
import com.vubq.joyboystore.entities.ProductDetail;
import com.vubq.joyboystore.entities.Voucher;
import com.vubq.joyboystore.enums.*;
import com.vubq.joyboystore.services.OrderDetailService;
import com.vubq.joyboystore.services.OrderService;
import com.vubq.joyboystore.services.ProductDetailService;
import com.vubq.joyboystore.services.VoucherService;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.DataTableResponse;
import com.vubq.joyboystore.utils.Response;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Transactional
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
                if (voucher.getQuantity() <= 0) {
                    return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá đã hết.");
                }
                orderNew.setVoucherId(voucher.getId());
                voucher.setQuantity(voucher.getQuantity() - 1);
            }
        }

        List<OrderDetail> listOrderDetail = new ArrayList<>();
        List<ProductDetail> listProductDetail = new ArrayList<>();
        for(int i = 0; i < dto.getListOrderDetail().size(); i++) {
            ProductDetail productDetail = this.productDetailService.getById(dto.getListOrderDetail().get(i).getProductDetailId());
            if(dto.getListOrderDetail().get(i).getQuantity() > productDetail.getQuantity()) {
                return Response.build()
                        .code(Response.CODE_INTERNAL_ERROR)
                        .data("Số lượng sản phẩm [ " + productDetail.getProduct().getName() + " - " + productDetail.getSize().getName() + " - " + productDetail.getColor().getName() + " - " + productDetail.getMaterial().getName() + " ] không đủ.");
            }
            productDetail.setQuantity(productDetail.getQuantity() - dto.getListOrderDetail().get(i).getQuantity());
            listProductDetail.add(productDetail);
            OrderDetail orderDetail = OrderDetail.builder()
                    .productDetail(productDetail)
                    .quantity(dto.getListOrderDetail().get(i).getQuantity())
                    .totalAmount(dto.getListOrderDetail().get(i).getTotalAmount())
                    .totalAmountNet(dto.getListOrderDetail().get(i).getTotalAmountNet())
                    .productDetailPrice(dto.getListOrderDetail().get(i).getProductDetailPrice())
                    .productDetailPriceNet(dto.getListOrderDetail().get(i).getProductDetailPriceNet())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .status(EStatus.ACTIVE)
                    .build();
            listOrderDetail.add(orderDetail);

        }

        Order order = this.orderService.save(orderNew);
        if(voucher != null) {
            this.voucherService.save(voucher);
        }
        listOrderDetail.forEach(od -> od.setOrder(order));
        this.orderDetailService.saveAll(listOrderDetail);
        this.productDetailService.saveAll(listProductDetail);
        return Response.build().ok();
    }

    @PostMapping("order-online")
    @Transactional
    public Response orderOnline(@RequestBody OrderSATCDto dto) {
        Order orderNew = Order.builder()
                .fullName(dto.getFullName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .ward(dto.getWard())
                .phoneNumber(dto.getPhoneNumber())
                .totalAmount(dto.getTotalAmount())
                .totalAmountNet(dto.getTotalAmountNet())
                .noteByAdmin(dto.getNoteByAdmin())
                .noteByCustomer(dto.getNoteByCustomer())
                .createdAt(new Date())
                .createdBy(this.getTheCurrentlyLoggedInUserId())
                .status(EOrderStatus.WAIT_FOR_CONFIRMATION)
                .type(EOrderType.ONLINE)
                .paymentType(dto.getPaymentType())
                .paymentType2(EPaymentType2.CASH)
                .build();

        Voucher voucher = null;
        if (dto.getIsVoucher()) {
            voucher = this.voucherService.getById(dto.getVoucherId());
            if (voucher == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá không tồn tại.");
            } else {
                if (voucher.getQuantity() <= 0) {
                    return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá đã hết.");
                }
                orderNew.setVoucherId(voucher.getId());
                voucher.setQuantity(voucher.getQuantity() - 1);
            }
        }

        List<OrderDetail> listOrderDetail = new ArrayList<>();
        for(int i = 0; i < dto.getListOrderDetail().size(); i++) {
            ProductDetail productDetail = this.productDetailService.getById(dto.getListOrderDetail().get(i).getProductDetailId());
            OrderDetail orderDetail = OrderDetail.builder()
                    .productDetail(productDetail)
                    .quantity(dto.getListOrderDetail().get(i).getQuantity())
                    .totalAmount(dto.getListOrderDetail().get(i).getTotalAmount())
                    .totalAmountNet(dto.getListOrderDetail().get(i).getTotalAmountNet())
                    .productDetailPrice(dto.getListOrderDetail().get(i).getProductDetailPrice())
                    .productDetailPriceNet(dto.getListOrderDetail().get(i).getProductDetailPriceNet())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .status(EStatus.ACTIVE)
                    .build();
            listOrderDetail.add(orderDetail);
        }

        Order order = this.orderService.save(orderNew);
        if(voucher != null) {
            this.voucherService.save(voucher);
        }
        listOrderDetail.forEach(od -> od.setOrder(order));
        this.orderDetailService.saveAll(listOrderDetail);
        return Response.build().ok();
    }

    @GetMapping("get-all-page-online")
    public DataTableResponse getAllPage(
            DataTableRequest request,
            @RequestParam String status,
            @RequestParam String dateFrom,
            @RequestParam String dateTo
    ) {
        Page<Order> result = this.orderService.getAllPage(request, status, dateFrom, dateTo, EOrderType.ONLINE);

        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.get().toList());
    }
}
