package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.dtos.Top10P;
import com.vubq.joyboystore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query(value = "SELECT DISTINCT p.id FROM Product p JOIN ProductDetail pd ON p.id = pd.product.id WHERE pd.quantity > 0")
    List<String> getAllProductIdInOfStock();

    @Query(value = "SELECT p FROM Product p JOIN ProductDetail pd ON p.id = pd.product.id JOIN OrderDetail od ON od.productDetail.id = pd.id GROUP BY p.id ORDER BY SUM(od.quantity) DESC LIMIT 10")
    List<Product> getTopQuantitySellProduct();

    @Query(value = "SELECT p FROM Product p JOIN Feedback f ON p.id = f.productDetail.product.id GROUP BY p.id ORDER BY AVG(f.rate) DESC LIMIT 10")
    List<Product> getTopRateProduct();

    @Query(value = "SELECT p FROM Product p WHERE p.status = com.vubq.joyboystore.enums.EStatus.ACTIVE ORDER BY p.createdAt DESC LIMIT 5")
    List<Product> getTop5ProductCreatedAtDESC();

    @Query(value = "SELECT p.id FROM OrderDetail od JOIN ProductDetail pd ON pd.id = od.productDetail.id JOIN Product p ON p.id = pd.product.id WHERE p.status = com.vubq.joyboystore.enums.EStatus.ACTIVE GROUP BY p.id ORDER BY SUM(od.quantity) DESC LIMIT 5")
    List<String> getTop5ProductBestSellingString();

    List<Product> getAllByIdIn(List<String> ids);

    @Query(value = "SELECT new com.vubq.joyboystore.dtos.Top10P(p.name, SUM(od.quantity), SUM(od.totalAmountNet)) FROM OrderDetail od JOIN ProductDetail pd ON pd.id = od.productDetail.id JOIN Product p ON p.id = pd.product.id JOIN Order o ON o.id = od.order.id WHERE p.status = com.vubq.joyboystore.enums.EStatus.ACTIVE AND o.status = com.vubq.joyboystore.enums.EOrderStatus.SUCCESS AND od.createdAt BETWEEN :dateFrom AND :dateTo GROUP BY p.id ORDER BY SUM(od.quantity) DESC LIMIT 5")
    List<Top10P> getTop10ProductBestSelling(Date dateFrom, Date dateTo);
}
