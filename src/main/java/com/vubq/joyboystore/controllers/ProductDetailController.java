package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.services.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/product-detail")
public class ProductDetailController extends BaseController {

    @Autowired
    private ProductDetailService productDetailService;

    
}
