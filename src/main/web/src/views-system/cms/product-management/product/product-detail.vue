<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Chi tiết Sản phẩm</h4>
        <span style="font-size: 14px;">Thêm mới Sản phẩm</span>
      </div>
    </div>

    <div class="container-form-table" style="padding-top: 0;">
      <el-collapse v-model="activeNames" style="border-top: 0;" @change="handleChange">
        <el-collapse-item name="product-information">
          <template slot="title">
            <i class="el-icon-info" />
            <span style="font-size: 14px; font-weight: bold;"> Thông tin Sản phẩm</span>
          </template>

          <el-divider />

          <el-form
            ref="dataForm"
            :model="material"
            label-position="left"
            :v-model="material"
            :rules="rules"
          >
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="Tên Sản phẩm" prop="name">
                  <el-input v-model="product.name" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="Giá tiền" prop="name">
                  <el-input v-model="product.name" />
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="Danh mục" prop="name">
                  <el-input v-model="product.name" />
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="Thương hiệu" prop="name">
                  <el-input v-model="product.name" />
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
          c
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'

export default {
  name: 'ProductManagementSizePage',
  components: { Tinymce },
  data() {
    return {
      activeNames: ['product-information', 'variant'],
      product: {}
    }
  },
  watch: {
  },
  created() {
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
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

  ::v-deep .mce-edit-area, .mce-container, .mce-panel, .mce-stack-layout-item {
    border-width: 0px 1px 0px 0px !important;
  }

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
    box-shadow: 0 1px 0px rgba(0, 0, 0, 0.2);
  }

  ::v-deep .mce-edit-area, .mce-container, .mce-panel, .mce-stack-layout-item {
    font-family: "Nunito", sans-serif !important;
  }

  ::v-deep .mce-colorbutton .mce-preview {
    left: 51%;
  }
</style>
