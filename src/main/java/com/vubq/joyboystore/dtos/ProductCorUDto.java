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
    private Double price;
    private String categoryId;
    private String brandId;
    private List<ImageDto> listImage;
    private List<ImageDto> listImageDelete;
    private List<ProductDetailCorUDto> listProductDetail;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private EStatus status;
}
