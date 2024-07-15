<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Chi tiết Sản phẩm</h4>
        <span style="font-size: 14px;">Thêm mới Sản phẩm</span>
      </div>
    </div>

    <div class="container-form-table" style="padding-top: 0;">
      <el-collapse v-model="activeNames" style="border-top: 0;">
        <el-collapse-item name="product-information">
          <template slot="title">
            <i class="el-icon-info" />
            <span style="font-size: 14px; font-weight: bold;"> Thông tin Sản phẩm</span>
          </template>

          <el-divider />

          <el-form
            ref="dataForm"
            label-position="left"
            style="margin-top: 20px;"
          >
            <el-row :gutter="40">
              <el-col :span="8">
                <el-form-item prop="name">
                  <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Tên sản phẩm</div>
                  <el-input v-model="product.name" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="40">
              <el-col :span="8">
                <el-form-item prop="name">
                  <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Giá tiền</div>
                  <el-input v-model="product.price" />
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item prop="name">
                  <div style="font-weight: bold;">
                    <div style="float: left;"><span style="color: red;">*</span>Danh mục</div>
                    <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" />Thêm mới</div>
                  </div>

                  <el-select
                    v-model="product.category"
                    style="width: 100%;"
                    placeholder="Chọn Danh mục"
                  >
                    <el-option
                      v-for="c in listCategoryActive"
                      :key="c.id"
                      :label="c.name"
                      :value="c.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item prop="name">
                  <div style="font-weight: bold;">
                    <div style="float: left;"><span style="color: red;">*</span>Thương hiệu</div>
                    <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" />Thêm mới</div>
                  </div>

                  <el-select
                    v-model="product.brand"
                    style="width: 100%;"
                    placeholder="Chọn Thương hiệu"
                  >
                    <el-option
                      v-for="b in listBrandActive"
                      :key="b.id"
                      :label="b.name"
                      :value="b.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="Mô tả" prop="name">
              <Tinymce ref="editor" :height="200" style="margin-top: 40px;" />
            </el-form-item>
          </el-form>
        </el-collapse-item>

        <el-collapse-item name="variant" class="collapse-item-custom">
          <template slot="title">
            <i class="el-icon-info" />
            <span style="font-size: 14px; font-weight: bold;"> Các loại thuộc tính</span>
          </template>

          <el-divider />

          <div v-for="(pd, i) in listProductDetail" :key="i">
            <div style="margin-top: 20px; margin-bottom: 20px;">
              <el-form
                :ref="'dataFormPD' + i"
                label-position="left"
                style="margin-top: 20px;"
              >
                <el-row :gutter="40">
                  <el-col :span="9">
                    <el-row :gutter="20">
                      <el-col :span="24">
                        <el-row :gutter="20">
                          <el-col :span="8">
                            <el-form-item>
                              <div style="font-weight: bold;">
                                <div style="float: left;"><span style="color: red;">*</span>Kích cỡ</div>
                                <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" /></div>
                              </div>

                              <el-select
                                v-model="pd.sizeId"
                                placeholder="Kích cỡ"
                                style="margin-bottom: 10px;"
                              >
                                <el-option
                                  v-for="s in listSizeActive"
                                  :key="s.id"
                                  :label="s.name"
                                  :value="s.id"
                                  :disabled="hideProperties('size', s.id, pd)"
                                />
                              </el-select>
                            </el-form-item>
                          </el-col>

                          <el-col :span="8">
                            <el-form-item>
                              <div style="font-weight: bold;">
                                <div style="float: left;"><span style="color: red;">*</span>Màu sắc</div>
                                <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" /></div>
                              </div>

                              <el-select
                                v-model="pd.colorId"
                                placeholder="Màu sắc"
                                style="margin-bottom: 10px;"
                              >
                                <el-option
                                  v-for="c in listColorActive"
                                  :key="c.id"
                                  :label="c.name"
                                  :value="c.id"
                                  :disabled="hideProperties('color', c.id, pd)"
                                />
                              </el-select>
                            </el-form-item>
                          </el-col>

                          <el-col :span="8">
                            <el-form-item>
                              <div style="font-weight: bold;">
                                <div style="float: left;"><span style="color: red;">*</span>Chất liệu</div>
                                <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" /></div>
                              </div>

                              <el-select
                                v-model="pd.materialId"
                                placeholder="Chất liệu"
                              >
                                <el-option
                                  v-for="m in listMaterialActive"
                                  :key="m.id"
                                  :label="m.name"
                                  :value="m.id"
                                  :disabled="hideProperties('material', m.id, pd)"
                                />
                              </el-select>
                            </el-form-item>
                          </el-col>
                        </el-row>
                      </el-col>

                      <el-col :span="24">
                        <el-row :gutter="20">
                          <el-col :span="8">
                            <el-form-item>
                              <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Số lượng</div>
                              <el-input />
                            </el-form-item>
                          </el-col>
                        </el-row>
                      </el-col>
                    </el-row>
                  </el-col>

                  <el-col :span="15">
                    <el-form-item>
                      <div style="float: left; font-weight: bold; width: 100%;">Ảnh</div>
                      <div>
                        <el-upload
                          ref="imageProductUpload"
                          action="#"
                          list-type="picture-card"
                          :auto-upload="false"
                          :file-list="product.images"
                        >
                          <i slot="default" class="el-icon-plus" />
                          <div slot="file" slot-scope="{file}">
                            <img
                              class="el-upload-list__item-thumbnail"
                              :src="file.url"
                              alt=""
                            >
                            <span class="el-upload-list__item-actions">
                              <span
                                class="el-upload-list__item-preview"
                                @click="handlePictureCardPreview(file)"
                              >
                                <i class="el-icon-zoom-in" />
                              </span>
                              <span
                                v-if="!disabled"
                                class="el-upload-list__item-delete"
                                @click="handleRemove(file)"
                              >
                                <i class="el-icon-delete" />
                              </span>
                            </span>
                          </div>
                        </el-upload>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </div>
            <el-divider />
          </div>

          <el-button style="margin-top: 20px; margin-bottom: 20px;" @click="addProductVariable">Thêm mới</el-button>

          <el-divider />
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import { brandGetAllStatusActive } from '@/api/brand'
import { categoryGetAllStatusActive } from '@/api/category'
import { colorGetAllStatusActive } from '@/api/color'
import { sizeGetAllStatusActive } from '@/api/size'
import { materialGetAllStatusActive } from '@/api/material'
import { ResponseCode } from '@/enums/enums'

export default {
  name: 'ProductManagementProductDetailPage',
  components: { Tinymce },
  data() {
    return {
      activeNames: ['product-information', 'variant'],
      product: {
        name: '',
        description: '',
        price: 0,
        brandId: '',
        categoryId: ''
      },
      listBrandActive: [],
      listCategoryActive: [],
      listColorActive: [],
      listSizeActive: [],
      listMaterialActive: [],
      listProductDetail: [],
      totalProductCanCreate: 0,
      totalSizeCanCreate: 0,
      totalColorCanCreate: 0,
      totalMaterialCanCreate: 0
    }
  },
  watch: {
  },
  async created() {
    await brandGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listBrandActive = res.data
      }
    })

    await categoryGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listCategoryActive = res.data
      }
    })

    await colorGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listColorActive = res.data
      }
    })

    await sizeGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listSizeActive = res.data
      }
    })

    await materialGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listMaterialActive = res.data
      }
    })

    this.calculateQuantity()
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    calculateQuantity() {
      this.totalProductCanCreate = this.listColorActive.length * this.listMaterialActive.length * this.listSizeActive.length
      this.totalColorCanCreate = this.listMaterialActive.length * this.listSizeActive.length
      this.totalSizeCanCreate = this.listMaterialActive.length * this.listColorActive.length
      this.totalMaterialCanCreate = this.listColorActive.length * this.listSizeActive.length
    },
    hideProperties(properties, idProperties, pd) {
      if (properties === 'size') {
        const total = this.listProductDetail.filter(e => e.sizeId === idProperties).length
        console.log(this.listProductDetail.find(e => e.sizeId === idProperties && e.materialId === pd.materialId && e.colorId === pd.colorId))
        return total === this.totalSizeCanCreate || this.listProductDetail.find(e => e.sizeId === idProperties && e.materialId === pd.materialId && e.colorId === pd.colorId)
      }

      if (properties === 'color') {
        const total = this.listProductDetail.filter(e => e.colorId === idProperties).length
        return total === this.totalColorCanCreate
      }

      if (properties === 'material') {
        const total = this.listProductDetail.filter(e => e.materialId === idProperties).length
        return total === this.totalMaterialCanCreate
      }

      return false
    },
    addProductVariable() {
      this.listProductDetail.push({
        colorId: '',
        sizeId: '',
        materialId: ''
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  ::v-deep .el-input__suffix {
    right: 15px;
  }

  ::v-deep .el-divider--horizontal {
    margin-bottom: 0;
    margin-top: 0;
  }

  ::v-deep .el-divider--horizontal {
    height: 0.5px;
  }

  ::v-deep .collapse-item-custom {
    .el-collapse-item__header, .el-collapse-item__wrap {
      border-bottom: none;
    }
  }

  ::v-deep .mce-tinymce {
    box-shadow: none !important;
    border: 1px solid #DCDFE6;
    border-radius: 8px;
  }

  ::v-deep .mce-tinymce {
    width: unset !important;
  }

  // ::v-deep .mce-edit-area, .mce-container, .mce-panel, .mce-stack-layout-item {
  //   border-width: 0px 1px 0px 0px !important;
  // }

  ::v-deep .editor-custom-btn-container {
    display: none;
  }

  ::v-deep .mce-branding, .mce-widget, .mce-label, .mce-flow-layout-item, .mce-last {
    display: none !important;
  }

  ::v-deep .mce-statusbar, .mce-container, .mce-panel, .mce-stack-layout-item, .mce-last {
    background-color: transparent;
  }

  ::v-deep .mce-top-part::before {
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.2);
  }

  ::v-deep .mce-edit-area, .mce-container, .mce-panel, .mce-stack-layout-item {
    font-family: "Nunito", sans-serif !important;
  }

  ::v-deep .mce-colorbutton .mce-preview {
    left: 51%;
  }

  ::v-deep .el-upload-list {
    transition: none !important;
  }

  ::v-deep .el-upload-list__item {
    transition: none !important;
  }

  ::v-deep .el-upload-list__item-name,
  .el-upload-list__item-status-label,
  .el-upload-list__item-actions {
    transition: none !important;
  }
</style>
