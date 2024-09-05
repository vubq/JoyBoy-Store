package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.repositories.OrderDetailRepository;
import com.vubq.joyboystore.repositories.ProductDetailRepository;
import com.vubq.joyboystore.repositories.ProductRepository;
import com.vubq.joyboystore.repositories.UserRepository;
import com.vubq.joyboystore.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/statistical")
public class StatisticalController extends BaseController {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("top-10-product-bet-selling")
    public Response top10ProductBetSelling(
            @RequestParam String dateFrom,
            @RequestParam String dateTo
    ) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return Response.build().ok().data(this.productRepository.getTop10ProductBestSelling(formatter.parse(dateFrom), formatter.parse(dateTo)));
    }

    @GetMapping("get-total-products-sold")
    public Response totalProductsSold(
            @RequestParam String dateFrom,
            @RequestParam String dateTo
    ) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return Response.build().ok().data(this.orderDetailRepository.totalProductsSold(formatter.parse(dateFrom), formatter.parse(dateTo)));
    }

    @GetMapping("get-total-revenue-products")
    public Response totalRevenueProducts(
            @RequestParam String dateFrom,
            @RequestParam String dateTo
    ) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return Response.build().ok().data(this.orderDetailRepository.totalRevenueProducts(formatter.parse(dateFrom), formatter.parse(dateTo)));
    }
}
