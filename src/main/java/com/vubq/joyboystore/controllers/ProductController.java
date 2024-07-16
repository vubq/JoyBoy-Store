package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.ProductCorUDto;
import com.vubq.joyboystore.entities.Image;
import com.vubq.joyboystore.entities.Product;
import com.vubq.joyboystore.entities.ProductDetail;
import com.vubq.joyboystore.enums.EImageType;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.services.*;
import com.vubq.joyboystore.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private MaterialService materialService;

    @PostMapping
    public Response createOrUpdate(@RequestBody ProductCorUDto dto) {
        List<Image> listImageNew = new ArrayList<>();
        List<Image> listImageDelete = new ArrayList<>();

        Product productNew = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .brand(this.brandService.getById(dto.getBrandId()))
                .category(this.categoryService.getById((dto.getCategoryId())))
                .build();
        if (StringUtils.isEmpty(dto.getId())) {
            productNew.setCreatedAt(new Date());
            productNew.setCreatedBy(this.getTheCurrentlyLoggedInUserId());
            productNew.setStatus(EStatus.ACTIVE);
        } else {
            Product productOld = this.productService.getById(dto.getId());
            productNew.setId(productOld.getId());
            productNew.setCreatedAt(productOld.getCreatedAt());
            productNew.setCreatedBy(productOld.getCreatedBy());
            productNew.setUpdatedAt(new Date());
            productNew.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
            productNew.setStatus(dto.getStatus());
        }
        Product product = this.productService.save(productNew);
        if(!dto.getListImage().isEmpty()) {
            dto.getListImage().forEach(i -> {
                if(StringUtils.isEmpty(i.getId())) {
                    Image imageNew = Image.builder()
                            .url(i.getUrl())
                            .type(EImageType.PRODUCT)
                            .publicId(i.getPublicId())
                            .secondaryId(product.getId())
                            .build();
                    listImageNew.add(imageNew);
                }
            });
        }
        if (!dto.getListImageDelete().isEmpty()) {
            dto.getListImageDelete().forEach(i -> {
                this.cloudinaryService.deleteByPublicId(i.getPublicId());
                Image imageDelete = Image.builder()
                        .id(i.getId())
                        .url(i.getUrl())
                        .type(i.getType())
                        .publicId(i.getPublicId())
                        .secondaryId(i.getSecondaryId())
                        .build();
                listImageDelete.add(imageDelete);
            });
        }

        if(!dto.getListProductDetail().isEmpty()) {
            dto.getListProductDetail().forEach(pd -> {
                ProductDetail productDetailNew = ProductDetail.builder()
                        .product(product)
                        .size(this.sizeService.getById(pd.getSizeId()))
                        .color(this.colorService.getById(pd.getColorId()))
                        .material(this.materialService.getById(pd.getMaterialId()))
                        .price(pd.getPrice())
                        .quantity(pd.getQuantity())
                        .build();
                if(StringUtils.isEmpty(pd.getId())) {
                    productDetailNew.setCreatedAt(new Date());
                    productDetailNew.setCreatedBy(this.getTheCurrentlyLoggedInUserId());
                    productDetailNew.setStatus(EStatus.ACTIVE);
                } else {
                    ProductDetail productDetailOld = this.productDetailService.getById(pd.getId());
                    productDetailNew.setId(productDetailOld.getId());
                    productDetailNew.setCreatedAt(productDetailOld.getCreatedAt());
                    productDetailNew.setCreatedBy(productDetailOld.getCreatedBy());
                    productDetailNew.setUpdatedAt(new Date());
                    productDetailNew.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
                    productDetailNew.setStatus(pd.getStatus());
                }
                ProductDetail productDetail = this.productDetailService.save(productDetailNew);
                if(!pd.getListImage().isEmpty()) {
                    pd.getListImage().forEach(i -> {
                        if(StringUtils.isEmpty(i.getId())) {
                            Image imageNew = Image.builder()
                                    .url(i.getUrl())
                                    .type(EImageType.PRODUCT_DETAIL)
                                    .publicId(i.getPublicId())
                                    .secondaryId(productDetail.getId())
                                    .build();
                            listImageNew.add(imageNew);
                        }
                    });
                }
                if(!pd.getListImageDelete().isEmpty()) {
                    pd.getListImageDelete().forEach(i -> {
                        this.cloudinaryService.deleteByPublicId(i.getPublicId());
                        Image imageDelete = Image.builder()
                                .id(i.getId())
                                .url(i.getUrl())
                                .type(i.getType())
                                .publicId(i.getPublicId())
                                .secondaryId(i.getSecondaryId())
                                .build();
                        listImageDelete.add(imageDelete);
                    });
                }
            });
        }

        this.imageService.saveAll(listImageNew);
        this.imageService.deleteAll(listImageDelete);
        return Response.build().ok();
    }
}
