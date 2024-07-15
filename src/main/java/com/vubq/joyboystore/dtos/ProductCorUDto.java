package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCorUDto {

    private String id;
    private String name;
    private String description;
    private String image;
    private Double price;
    private String categoryId;
    private String brandId;
    private List<ImageDto> images;
    private List<ProductDetailCorUDto> productDetails;
    private List<String> sizes;
    private List<String> colors;
    private List<String> materials;
    private List<ImageDto> imageDeletes;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private EStatus status;
}
