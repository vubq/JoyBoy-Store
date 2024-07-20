<template>
  <div>
    <div style="font-size: 12px; margin-bottom: 10px;">TRANG CHỦ</div>
    <div>
      <div class="tag-product">
        <el-row :gutter="60">
          <el-col :span="10">
            <el-image style="width: 100%;" :src="listImage[0]" />
          </el-col>

          <el-col :span="14">
            <div style="font-size: 16px; font-weight: bold;">
              {{ product ? product.name : '' }}
            </div>

            <div style="margin-top: 10px; display: flex; align-items: center; font-size: 14px;">
              <el-rate
                :value="product ? product.rate : 0"
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                text-color="#ff9900"
                disabled
              />
              {{ product ? product.rate + '/5' : '0/5' }}
              <div style="border-left: 1px solid gray; margin-left: 20px; padding-left: 20px;">
                {{ product ? product.quantitySold : 0 }} Đã bán
              </div>
            </div>

            <div style="margin-top: 10px; font-size: 18px; font-weight: bold;">
              <div v-if="pd">
                <span style="color: #ee4d2d;">{{ formatCurrencyVND(pd.priceNet) }}</span>
                <span v-if="pd.price > pd.priceNet" style="text-decoration: line-through; color: rgba(0,0,0,.26); margin-left: 10px; font-size: 16px;">{{ formatCurrencyVND(pd.price) }}</span>
              </div>

              <div v-else>
                <span style="color: #ee4d2d;">{{ formatCurrencyVND(product ? product.priceNet : 0) }}</span>
                <span v-if="product.price > product.priceNet" style="text-decoration: line-through; color: rgba(0,0,0,.26); margin-left: 10px; font-size: 16px;">{{ formatCurrencyVND(product ? product.price : 0) }}</span>
              </div>
            </div>

            <div style="margin-top: 20px;">
              <span style="font-size: 14px;">Kích cỡ:</span>
              <div style="display: flex; margin-top: 5px; font-size: 14px;">
                <div v-for="(s, i) in sizes" :key="s.id">
                  <div style="margin-right: 10px;" :class="[sizeSelected === s.id ? 'tag tag-active' : 'tag', s.isOutOfStock || !genIsOutOfStockSize(s.id) ? 'tag-disable' : '']" @click="changeSizeSelected(s.id, s.isOutOfStock)">
                    {{ s.name }}
                  </div>
                </div>
              </div>
            </div>

            <div style="margin-top: 10px;">
              <span style="font-size: 14px;">Màu sắc:</span>
              <div style="display: flex; margin-top: 5px; font-size: 14px;">
                <div v-for="(c, i) in colors" :key="c.id">
                  <div style="margin-right: 10px;" :class="[colorSelected === c.id ? 'tag tag-active' : 'tag', c.isOutOfStock || !genIsOutOfStockColor(c.id) ? 'tag-disable' : '']" class="''" @click="changeColorSelected(c.id, c.isOutOfStock)">
                    {{ c.name }}
                  </div>
                </div>
              </div>
            </div>

            <div style="margin-top: 10px;">
              <span style="font-size: 14px;">Chất Liệu:</span>
              <div style="display: flex; margin-top: 5px; font-size: 14px;">
                <div v-for="(m, i) in materials" :key="m.id">
                  <div style="margin-right: 10px;" :class="[materialSelected === m.id ? 'tag tag-active' : 'tag', m.isOutOfStock || !genIsOutOfStockMaterial(m.id) ? 'tag-disable' : '']" @click="changeMaterialSelected(m.id, m.isOutOfStock)">
                    {{ m.name }}
                  </div>
                </div>
              </div>
            </div>

            <div style="margin-top: 10px;">
              <span style="font-size: 14px;">Số lượng:</span>
              <div style="margin-top: 5px;">
                <div style="display: flex; align-items: center">
                  <el-button
                    plain
                    type="info"
                    style="padding-left: 4px; padding-right: 4px;"
                    @click="--quantityPurchased"
                  >
                    <i class="el-icon-minus" />
                  </el-button>
                  <el-input
                    v-model="quantityPurchased"
                    style="text-align: center !important; margin-left: 4px; margin-right: 4px; width: 70px; border-radius: 0 !important;"
                    class="input-text-align-center input-quantity"
                  />
                  <el-button
                    plain
                    type="info"
                    style="padding-left: 4px; padding-right: 4px;"
                    @click="++quantityPurchased"
                  >
                    <i class="el-icon-plus" />
                  </el-button>
                  <span style="margin-left: 20px; font-size: 14px;">{{ totalProductsAvailable }} sản phẩm có sẵn</span>
                </div>
              </div>
            </div>
            <div style="margin-top: 20px; padding: 10px 0;">
              <span style="padding: 10px 25px; background-color: #11A983; color: #fff; border-radius: 4px; font-size: 18px; cursor: pointer;" @click="addToCart()">
                <i class="el-icon-shopping-cart-full" />
                Thêm vào giỏ hàng
              </span>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { productGetProductViewById, getProductByIdWebShop } from '@/api/product'
import { filterProductAttributes, getProductDetailByAttributes } from '@/api/product-detail'
import { ResponseCode } from '@/enums/enums'
import { formatCurrencyVND } from '@/utils/format'
import moment from 'moment'
import { mapActions } from 'vuex'

export default {
  name: 'ProductViewPage',
  components: { },
  data() {
    return {
      formatCurrencyVND: formatCurrencyVND,
      Moment: moment,
      productId: this.$route.params.id ? this.$route.params.id : '',
      product: null,
      listImage: [],
      sizes: [],
      colors: [],
      materials: [],
      listOfAvailableSizes: [],
      listOfAvailableColors: [],
      listOfAvailableMaterials: [],
      listOfDisableSizes: [],
      listOfDisableColors: [],
      listOfDisableMaterials: [],
      colorSelected: '',
      sizeSelected: '',
      materialSelected: '',
      totalProductsAvailable: 0,
      quantityPurchased: 1,
      listOfFeedbacks: [],
      pd: null
    }
  },
  watch: {
  },
  created() {
    if (this.productId) {
      getProductByIdWebShop(this.productId)
        .then(res => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            this.product = res.data
            this.listImage = res.data.listImage
            this.sizes = res.data.sizes
            this.colors = res.data.colors
            this.materials = res.data.materials
            this.totalProductsAvailable = res.data.totalProductsAvailable
          }
        })
    }
    // getListOfFeedbacksByCriteria({
    //   currentPage: 1,
    //   perPage: 10,
    //   filter: '',
    //   sortBy: '',
    //   sortDesc: true,
    //   productId: this.productId,
    //   rate: null
    // }).then(res => {
    //   if(res.data && res.data.code === ResponseCode.CODE_SUCCESS) {
    //     this.listOfFeedbacks = res.data.items
    //   }
    // })
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    ...mapActions([
      'addProductToCart'
    ]),
    changeSizeSelected(id, isOutOfStock) {
      if (!isOutOfStock && this.genIsOutOfStockSize(id)) {
        if (id === this.sizeSelected) {
          this.sizeSelected = ''
        } else {
          this.sizeSelected = id
        }
        this.filterAttributes()
      }
    },
    changeColorSelected(id, isOutOfStock) {
      if (!isOutOfStock && this.genIsOutOfStockColor(id)) {
        if (id === this.colorSelected) {
          this.colorSelected = ''
        } else {
          this.colorSelected = id
        }
        this.filterAttributes()
      }
    },
    changeMaterialSelected(id, isOutOfStock) {
      if (!isOutOfStock && this.genIsOutOfStockMaterial(id)) {
        if (id === this.materialSelected) {
          this.materialSelected = ''
        } else {
          this.materialSelected = id
        }
        this.filterAttributes()
      }
    },
    async filterAttributes() {
      await filterProductAttributes({
        productId: this.productId,
        sizeId: this.sizeSelected,
        colorId: this.colorSelected,
        materialId: this.materialSelected
      }).then(async res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          if (!this.sizeSelected) {
            this.listOfDisableSizes = this.sizes.map(e => e.id)
            res.data.listOfAvailableSizes.forEach(e => {
              this.listOfDisableSizes = this.listOfDisableSizes.filter(x => x !== e)
            })
          }
          if (!this.colorSelected) {
            this.listOfDisableColors = this.colors.map(e => e.id)
            res.data.listOfAvailableColors.forEach(e => {
              this.listOfDisableColors = this.listOfDisableColors.filter(x => x !== e)
            })
          }
          if (!this.materialSelected) {
            this.listOfDisableMaterials = this.materials.map(e => e.id)
            res.data.listOfAvailableMaterials.forEach(e => {
              this.listOfDisableMaterials = this.listOfDisableMaterials.filter(x => x !== e)
            })
          }
          if (this.sizeSelected && this.colorSelected && this.materialSelected) {
            //
          }
          this.totalProductsAvailable = res.data.totalProductsAvailable
        }
      })
      await this.loadImageAndPrice()
    },
    genIsOutOfStockSize(id) {
      return !this.listOfDisableSizes.find(e => e === id)
    },
    genIsOutOfStockColor(id) {
      return !this.listOfDisableColors.find(e => e === id)
    },
    genIsOutOfStockMaterial(id) {
      return !this.listOfDisableMaterials.find(e => e === id)
    },
    addToCart() {
      if (this.sizeSelected && this.colorSelected && this.materialSelected) {
        getProductDetailByAttributes({
          productId: this.productId,
          sizeId: this.sizeSelected,
          colorId: this.colorSelected,
          materialId: this.materialSelected
        }).then(res => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            this.addProductToCart({
              productDetailId: res.data.id,
              quantity: this.quantityPurchased
            })
          }
        })
      }
    },
    loadImageAndPrice() {
      if (this.productId && this.sizeSelected && this.colorSelected && this.materialSelected) {
        getProductDetailByAttributes({
          productId: this.productId,
          sizeId: this.sizeSelected,
          colorId: this.colorSelected,
          materialId: this.materialSelected
        }).then(res => {
          if (res && res.data && res.code === ResponseCode.CODE_SUCCESS) {
            this.pd = res.data
          }
        })
      } else {
        this.pd = null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.tag-product {
  background-color: #ffffff;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .08);
  padding: 20px;
}

::v-deep .custom-divider, .el-divider--horizontal {
  margin: 0;
  height: 0.5px;
}

.image-custom {
  width: 100%;
  height: 200px;
  overflow: hidden;
  position: relative;
}

.image-custom img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease-in-out;
}

.image-custom:hover img {
    transform: scale(1.1); /* Phóng to ảnh khi hover */
}

.image-container {
  position: relative; display: inline-block;
}

.combo-button {
  background-color: #11A983;
  position: absolute;
  top: 80%;
  left: 20px;
  right: 20px;
  display: none;
  transition: opacity 0.5s ease-in-out;
  border-radius: 4px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .08);
  padding: 4px;
}

.image-container:hover .combo-button {
  display: block
}

.image-container img {
  transition: transform 0.2s;
}

.image-container:hover img {
  transform: scale(1.1);
}
.tag {
  cursor: pointer;
  padding: 5px 15px;
  border: 1px solid rgba(0,0,0,.09);
  border-radius: 2px;
}
.tag:hover {
  border-color: #11A983;
  color: #11A983;
}
.tag-active {
  border-color: #11A983;
  color: #11A983;
}
.tag-disable {
  cursor: not-allowed;
  background-color: rgba(0,0,0,.09);
}
.tag-disable:hover {
  border-color: rgba(0,0,0,.09);
  color: #000;
}
::v-deep .input-text-align-center {
  input {
    text-align: center !important;
  }
}
</style>
