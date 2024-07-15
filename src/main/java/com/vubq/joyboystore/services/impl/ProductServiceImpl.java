package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Product;
import com.vubq.joyboystore.repositories.ProductRepository;
import com.vubq.joyboystore.services.ProductService;
import com.vubq.joyboystore.utils.BaseSpecification;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.SearchCriteria;
import com.vubq.joyboystore.utils.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> getListOfProductsByCriteria(DataTableRequest request, String status) {
        request.setSortBy("createdAt");
        request.setSortDesc(true);
        PageRequest pageable = request.toPageable();
        BaseSpecification<Product> specNameContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Product.Fields.name})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Product> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Product.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.productRepository.findAll(Specification.where(specNameContains).and(specStatusEquality), pageable);
    }

    @Override
    public Product save(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public Product getById(String id) {
        return this.productRepository.findById(id).orElse(null);
    }

    @Override
    public List<String> getAllProductIdInOfStock() {
        return this.productRepository.getAllProductIdInOfStock();
    }
}
