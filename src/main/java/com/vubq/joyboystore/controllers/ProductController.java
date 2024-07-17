package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.ImageDto;
import com.vubq.joyboystore.dtos.ProductCorUDto;
import com.vubq.joyboystore.dtos.ProductDetailCorUDto;
import com.vubq.joyboystore.dtos.ProductViewDto;
import com.vubq.joyboystore.entities.Color;
import com.vubq.joyboystore.entities.Image;
import com.vubq.joyboystore.entities.Product;
import com.vubq.joyboystore.entities.ProductDetail;
import com.vubq.joyboystore.enums.EImageType;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.services.*;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.DataTableResponse;
import com.vubq.joyboystore.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        if (!dto.getListImage().isEmpty()) {
            dto.getListImage().forEach(i -> {
                if (StringUtils.isEmpty(i.getId())) {
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

        if (!dto.getListProductDetail().isEmpty()) {
            dto.getListProductDetail().forEach(pd -> {
                ProductDetail productDetailNew = ProductDetail.builder()
                        .product(product)
                        .size(this.sizeService.getById(pd.getSizeId()))
                        .color(this.colorService.getById(pd.getColorId()))
                        .material(this.materialService.getById(pd.getMaterialId()))
                        .price(pd.getPrice())
                        .quantity(pd.getQuantity())
                        .build();
                if (StringUtils.isEmpty(pd.getId())) {
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
                if (!pd.getListImage().isEmpty()) {
                    pd.getListImage().forEach(i -> {
                        if (StringUtils.isEmpty(i.getId())) {
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
                if (!pd.getListImageDelete().isEmpty()) {
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
        return Response.build().ok().data(product);
    }

    @GetMapping("{id}")
    public Response getById(@PathVariable String id) {
        Product product = this.productService.getById(id);
        if (product == null) {
            ProductCorUDto productCorUDto = new ProductCorUDto();
            productCorUDto.setListImage(new ArrayList<>());
            productCorUDto.setListImageDelete(new ArrayList<>());
            productCorUDto.setListProductDetail(new ArrayList<>());
            return Response.build().ok().data(productCorUDto);
        }
        ProductCorUDto productCorUDto = ProductCorUDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brandId(product.getBrand().getId())
                .categoryId(product.getCategory().getId())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .listImage(new ArrayList<>())
                .listImageDelete(new ArrayList<>())
                .listProductDetail(new ArrayList<>())
                .status(product.getStatus())
                .build();
        this.imageService.getAllBySecondaryId(product.getId()).forEach(i -> {
            productCorUDto.addImageToList(ImageDto.builder()
                    .id(i.getId())
                    .url(i.getUrl())
                    .publicId(i.getPublicId())
                    .secondaryId(i.getSecondaryId())
                    .type(i.getType())
                    .build());
        });
        List<ProductDetail> listProductDetail = this.productDetailService.getAllByProductId(product.getId());
        listProductDetail.forEach(pd -> {
            ProductDetailCorUDto productDetailCorUDto = ProductDetailCorUDto.builder()
                    .id(pd.getId())
                    .price(pd.getPrice())
                    .quantity(pd.getQuantity())
                    .productId(pd.getProduct().getId())
                    .colorId(pd.getColor().getId())
                    .sizeId(pd.getSize().getId())
                    .materialId(pd.getMaterial().getId())
                    .createdAt(pd.getCreatedAt())
                    .updatedAt(pd.getUpdatedAt())
                    .createdBy(pd.getCreatedBy())
                    .updatedBy(pd.getUpdatedBy())
                    .listImage(new ArrayList<>())
                    .listImageDelete(new ArrayList<>())
                    .status(pd.getStatus())
                    .build();
            this.imageService.getAllBySecondaryId(pd.getId()).forEach(i -> {
                productDetailCorUDto.addImageToList(ImageDto.builder()
                        .id(i.getId())
                        .url(i.getUrl())
                        .publicId(i.getPublicId())
                        .secondaryId(i.getSecondaryId())
                        .type(i.getType())
                        .build());
            });
            productCorUDto.addProductDetailToList(productDetailCorUDto);
        });
        return Response.build().ok().data(productCorUDto);
    }

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(
            DataTableRequest request,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "brandId") String brandId,
            @RequestParam(value = "categoryId") String categoryId,
            @RequestParam(value = "sizeId") String sizeId,
            @RequestParam(value = "colorId") String colorId,
            @RequestParam(value = "materialId") String materialId,
            @RequestParam(value = "minPrice") Double minPrice,
            @RequestParam(value = "maxPrice") Double maxPrice
    ) {
        Page<ProductDetail> result = this.productDetailService.getAllPage(
                request,
                status,
                brandId,
                categoryId,
                sizeId,
                colorId,
                materialId,
                minPrice,
                maxPrice
        );

        List<ProductViewDto> listProductView = new ArrayList<>();
        result.getContent().forEach(pd -> {
            ProductViewDto productView = ProductViewDto.builder()
                    .id(pd.getProduct().getId())
                    .name(pd.getProduct().getName())
                    .description(pd.getProduct().getDescription())
                    .categoryName(pd.getProduct().getCategory().getName())
                    .brandName(pd.getProduct().getBrand().getName())
                    .status(pd.getProduct().getStatus())
                    .createdAt(pd.getProduct().getCreatedAt())
                    .updatedAt(pd.getProduct().getUpdatedAt())
                    .createdBy(pd.getProduct().getCreatedBy())
                    .updatedBy(pd.getProduct().getUpdatedBy())
                    .build();
            StringBuilder listColorString = new StringBuilder();
            List<Color> colors = this.colorService.getAllByIdIn(this.colorService.getAllByProductId(pd.getProduct().getId()));
            for (int i = 0; i < colors.size(); i++) {
                Color c = colors.get(i);
                if (i > 0) {
                    listColorString.append(", ");
                }
                listColorString.append(c.getName());
            }
            productView.setListColorString(listColorString.toString());
            listProductView.add(productView);
        });
        return DataTableResponse.build().ok()
                .totalRows(result.getTotalElements())
                .items(listProductView);
    }
}
