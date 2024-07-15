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
        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .brand(this.brandService.getById(dto.getBrandId()))
                .category(this.categoryService.getById((dto.getCategoryId())))
                .status(dto.getStatus())
                .build();
        if (StringUtils.isEmpty(dto.getId())) {
            product.setCreatedAt(new Date());
            product.setCreatedBy(this.getTheCurrentlyLoggedInUserId());
        } else {
            Product productOld = this.productService.getById(dto.getId());
            product.setId(productOld.getId());
            product.setCreatedAt(productOld.getCreatedAt());
            product.setCreatedBy(productOld.getCreatedBy());
            product.setUpdatedAt(new Date());
            product.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
        }
        Product productFinal = this.productService.save(product);

        List<Image> images = new ArrayList<>();
        dto.getImages().forEach((imageDto) -> {
            if (Strings.isEmpty(imageDto.getId())) {
                Image imageSave = Image.builder()
                        .url(imageDto.getUrl())
                        .type(EImageType.PRODUCT)
                        .publicId(imageDto.getPublicId())
                        .secondaryId(productFinal.getId())
                        .build();
                images.add(imageSave);
            }
        });

        dto.getProductDetails().forEach((productDetailDto) -> {
            ProductDetail productDetail = ProductDetail.builder()
                    .code(productDetailDto.getCode())
                    .quantity(productDetailDto.getQuantity())
                    .price(productDetailDto.getPrice())
                    .product(productFinal)
                    .size(this.sizeService.getById(productDetailDto.getSizeId()))
                    .color(this.colorService.getById(productDetailDto.getColorId()))
                    .material(this.materialService.getById(productDetailDto.getMaterialId()))
                    .status(EStatus.ACTIVE).build();
            if (Strings.isEmpty(productDetailDto.getId())) {
                productDetail.setCreatedAt(new Date());
                productDetail.setCreatedBy(this.getTheCurrentlyLoggedInUserId());
            } else {
                ProductDetail productDetailOld = this.productDetailService.getById(productDetailDto.getId());
                productDetail.setId(productDetailOld.getId());
                productDetail.setCreatedAt(productDetailOld.getCreatedAt());
                productDetail.setCreatedBy(productDetailOld.getCreatedBy());
            }
            this.productDetailService.save(productDetail);

//            for(ImageDto image: productDetailDto.getImages()) {
//                Image imageSave = Image.builder()
//                        .url(image.getUrl())
//                        .type(EImageType.PRODUCT_DETAIL)
//                        .publicId(image.getPublicId())
//                        .secondaryId(productDetail.getId())
//                        .build();
//                images.add(imageSave);
//            }
        });
        this.imageService.saveAll(images);

        List<Image> imageDeletes = new ArrayList<>();

        if (dto.getImageDeletes() != null) {
            dto.getImageDeletes().forEach((image) -> {
                this.cloudinaryService.deleteByPublicId(image.getPublicId());
                Image imageSave = Image.builder()
                        .id(image.getId())
                        .url(image.getUrl())
                        .type(image.getType())
                        .publicId(image.getPublicId())
                        .secondaryId(image.getSecondaryId())
                        .build();
                imageDeletes.add(imageSave);
            });
        }
        this.imageService.deleteAll(imageDeletes);
        return Response.build().ok();
    }
}
