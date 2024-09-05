package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    @Query(value = "SELECT od FROM OrderDetail od WHERE od.order.id = :orderId")
    List<OrderDetail> findAllByOrderId(String orderId);

    @Query(value = "SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.productDetail.product.id = :productId")
    Integer getQuantitySold(String productId);

    @Query(value = "SELECT SUM(od.quantity) FROM OrderDetail od JOIN Order o ON o.id = od.order.id WHERE o.status = com.vubq.joyboystore.enums.EOrderStatus.SUCCESS AND od.createdAt BETWEEN :dateFrom AND :dateTo")
    int totalProductsSold(Date dateFrom, Date dateTo);

    @Query(value = "SELECT SUM(od.totalAmountNet) FROM OrderDetail od JOIN Order o ON o.id = od.order.id WHERE o.status = com.vubq.joyboystore.enums.EOrderStatus.SUCCESS AND od.createdAt BETWEEN :dateFrom AND :dateTo")
    int totalRevenueProducts(Date dateFrom, Date dateTo);
}
