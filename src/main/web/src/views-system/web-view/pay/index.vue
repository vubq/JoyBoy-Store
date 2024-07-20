<template>
  <div>
    <div style="font-size: 12px; margin-bottom: 10px;">TRANG CHỦ</div>
    <div class="tag-product">
      <el-row :gutter="40">
        <el-col :span="12">
          <el-form>
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item>
                  <el-alert
                    title="Vui lòng đăng nhập và nhập thông tin để có thể theo dõi trạng thái đơn hàng."
                    type="warning"
                    show-icon
                    :closable="false"
                  />
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Họ và tên
                    <span style="color: red;">*</span>
                  </span>
                  <el-input v-model="orderW.fullName" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Số điện thoại
                    <span style="color: red;">*</span>
                  </span>
                  <el-input v-model="orderW.phoneNumber" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Email
                    <span style="color: red;">*</span>
                  </span>
                  <el-input v-model="orderW.email" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Tỉnh/Thành phố
                    <span style="color: red;">*</span>
                  </span>
                  <el-select v-model="cityCode" filterable style="width: 100%;">
                    <el-option
                      v-for="c in listCity"
                      :key="c.province_id"
                      :label="c.province_name"
                      :value="c.province_id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Quận/Huyện
                    <span style="color: red;">*</span>
                  </span>
                  <el-select v-model="districtCode" filterable style="width: 100%;">
                    <el-option
                      v-for="d in listDistrict"
                      :key="d.district_id"
                      :label="d.district_name"
                      :value="d.district_id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Phường/Xã
                    <span style="color: red;">*</span>
                  </span>
                  <el-select v-model="wardCode" filterable style="width: 100%;">
                    <el-option
                      v-for="w in listWard"
                      :key="w.ward_id"
                      :label="w.ward_name"
                      :value="w.ward_id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Địa chỉ
                    <span style="color: red;">*</span>
                  </span>
                  <el-input v-model="orderW.address" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Ghi chú
                  </span>
                  <el-input v-model="orderW.noteByCustomer" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>

        <el-col :span="12">
          <div style="background-color: #fffbf8; padding: 20px; border: 1px solid #f9ede5;">
            <span style="font-size: 16px;">ĐƠN HÀNG CỦA BẠN</span>
            <el-divider class="pay-divider" />
            <div>
              <el-row>
                <el-col :span="16">
                  <div style="font-size: 16px;">Sản phẩm</div>
                </el-col>
                <el-col :span="8" style="text-align: end;">
                  <div style="font-size: 16px;">Tổng tiền</div>
                </el-col>
                <div v-for="p in productInCart" :key="p.id">
                  <el-col :span="16" style="margin-top: 10px;">
                    <div style="">
                      {{ genQuantityPurchased(p.id) }}
                      <i class="el-icon-close" style="cursor: pointer;" />
                      {{ p.product.name }}
                    </div>
                  </el-col>
                  <el-col :span="8" style="text-align: end; margin-top: 10px;">
                    <div style="color: #ee4d2d;">
                      <span v-if="genTotalMoneyProduct(p.id, p.price) > genTotalMoneyProduct(p.id, p.priceNet)" style="text-decoration: line-through; color: rgba(0,0,0,.26);">{{ formatCurrencyVND(genTotalMoneyProduct(p.id, p.price)) }}</span>
                      {{ formatCurrencyVND(genTotalMoneyProduct(p.id, p.priceNet)) }}
                    </div>
                  </el-col>
                </div>
              </el-row>
            </div>
            <el-divider class="pay-divider" />
            <div>
              <div style="font-size: 16px; margin-bottom: 10px;">Mã giảm giá</div>
              <div>
                <el-row :gutter="20">
                  <el-col :span="18">
                    <el-input />
                  </el-col>
                  <el-col :span="6">
                    <div style="font-size: 18px; cursor: pointer; background-color: #11A983; color: #fff; border-radius: 4px; padding: auto; height: 36px; display: flex; align-items: center; justify-content: center;">Áp dụng</div>
                  </el-col>
                </el-row>
              </div>
            </div>
            <el-divider class="pay-divider" />
            <div>
              <div style="font-size: 16px;">Hình thức thanh toán</div>
              <el-radio-group v-model="orderW.paymentType" style="margin-top: 10px;">
                <el-radio :label="'PAYMENT_ON_DELIVERY'" class="pay-radio">Khi nhận hàng</el-radio>
                <el-radio :label="'PAYMENT_ONLINE'" class="pay-radio">Online</el-radio>
              </el-radio-group>
            </div>
            <el-divider class="pay-divider" />
            <div>
              <el-row>
                <el-col :span="16">
                  <div>Tổng tiền gốc</div>
                  <div style="margin-top: 10px;">Giảm giá</div>
                  <div style="margin-top: 10px;">Tổng thanh toán</div>
                </el-col>
                <el-col :span="8" style="text-align: end;">
                  <div style="font-size: 16px; color: #ee4d2d;">{{ formatCurrencyVND(genTotalMoney()) }}</div>
                  <div style="font-size: 16px; color: #ee4d2d; margin-top: 10px;">-{{ formatCurrencyVND(genTotalMoney() - genTotalMoneyNet()) }}</div>
                  <div style="font-size: 16px; color: #ee4d2d; margin-top: 10px;">{{ formatCurrencyVND(genTotalMoneyNet()) }}</div>
                </el-col>
              </el-row>
              <div style="font-size: 18px; cursor: pointer; margin-top: 10px; background-color: #11A983; color: #fff; border-radius: 4px; padding: auto; height: 36px; display: flex; align-items: center; justify-content: center;" @click="payment">
                {{ order.paymentType === 'PAYMENT_ONLINE' ? 'Thanh toán' : 'Đặt hàng' }}
              </div>
            </div>
            <el-divider class="pay-divider" />
            <div>
              <div>
                <i class="el-icon-phone-outline" />
                Hotline 1900 1000
              </div>
              <div style="margin-top: 10px;">
                <i class="el-icon-tickets" />
                Hướng dẫn đặt hàng
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getAllProductDetailByListId } from '@/api/product-detail'
import { paymentOnline } from '@/api/payment'
import { orderOnline } from '@/api/order'
import { ResponseCode, Status } from '@/enums/enums'
import { formatCurrencyVND } from '@/utils/format'
import { mapActions, mapGetters } from 'vuex'
import axios from 'axios'

export default {
  name: 'PayPage',
  components: { },
  data() {
    return {
      formatCurrencyVND: formatCurrencyVND,
      listCity: [],
      listDistrict: [],
      listWard: [],
      cityCode: '',
      districtCode: '',
      wardCode: '',
      city: '',
      district: '',
      ward: '',
      productInCart: [],
      typePay: 'PAYMENT_ON_DELIVERY',
      fullNameCustomer: '',
      phoneNumber: '',
      address: '',
      email: '',
      voucherId: '',
      noteByCustomer: '',
      paymentOnlineCode: '',
      orderW: {
        id: '',
        fullName: '',
        phoneNumber: '',
        email: '',
        address: '',
        city: '',
        district: '',
        ward: '',
        totalAmount: 0,
        totalAmountNet: 0,
        voucherId: '',
        noteByCustomer: '',
        noteByAdmin: '',
        createdAt: '',
        updatedAt: '',
        createdBy: '',
        updatedBy: '',
        completedAt: '',
        type: '',
        paymentType: 'PAYMENT_ON_DELIVERY',
        status: '',
        listOrderDetail: [],
        isVoucher: false,
        voucherCode: '',
        voucher: null,
        moneyPaid: 0,
        moneyRefunds: 0
      }
    }
  },
  computed: {
    ...mapGetters([
      'cart',
      'allInfor',
      'pay',
      'userId',
      'order'
    ])
  },
  watch: {
    cityCode: function() {
      axios.get('https://vapi.vnappmob.com/api/province/district/' + this.cityCode)
        .then(res => {
          if (res.data) {
            this.listDistrict = res.data.results
          }
        })
      this.orderW.city = this.listCity.find(e => e.province_id === this.cityCode).province_name
      this.changePay()
    },
    districtCode: function() {
      axios.get('https://vapi.vnappmob.com/api/province/ward/' + this.districtCode)
        .then(res => {
          if (res.data) {
            this.listWard = res.data.results
          }
        })
      this.orderW.district = this.listDistrict.find(e => e.district_id === this.districtCode).district_name
      this.changePay()
    },
    wardCode: function() {
      this.orderW.ward = this.listWard.find(e => e.ward_id === this.wardCode).ward_name
      this.changePay()
    },
    cart: function() {
      this.getListProductDetail()
      this.changePay()
    },
    typePay: function() {
      this.changePay()
    },
    fullNameCustomer: function() {
      this.changePay()
    },
    phoneNumber: function() {
      this.changePay()
    },
    address: function() {
      this.changePay()
    },
    email: function() {
      this.changePay()
    },
    voucherId: function() {
      this.changePay()
    },
    noteByCustomer: function() {
      this.changePay()
    },
    orderW: async function() {
      await this.setListOrderDetail()
      this.setOrder(this.orderW)
    }
  },
  async created() {
    axios.get('https://vapi.vnappmob.com/api/province/')
      .then(res => {
        if (res.data) {
          this.listCity = res.data.results
        }
      })
    if (this.cart.length > 0) {
      this.getListProductDetail()
    }
    console.log(this.$route.query.vnp_TransactionStatus)
    if (this.$route.query.vnp_TransactionStatus === '00') {
      await this.setListOrderDetail()
      orderOnline(this.order).then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.clearCart()
          this.$notify({
            title: 'Success',
            message: 'Đặt hàng thành công.',
            type: 'success'
          })
          this.$router.push({
            path: '/shop'
          })
        }
      })
    }
  },
  methods: {
    ...mapActions([
      'clearCart',
      'setPay',
      'setOrder'
    ]),
    formatCurrenyVND_d(value) {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
    },
    async getListProductDetail() {
      await getAllProductDetailByListId({
        listProductDetailId: this.cart.map(e => e.productDetailId)
      }).then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.productInCart = res.data
        }
      })
    },
    genQuantityPurchased(productDetailId) {
      console.log(this.cart.find(e => e.productDetailId === productDetailId))
      if (this.cart.length > 0) {
        return this.cart.find(e => e.productDetailId === productDetailId).quantity
      } else {
        return 0
      }
    },
    genTotalMoneyProduct(productDetailId, price) {
      if (this.cart.length > 0) {
        return this.cart.find(e => e.productDetailId === productDetailId).quantity * price
      } else {
        return 0
      }
    },
    genTotalMoney() {
      if (this.cart.length > 0) {
        let total = 0
        this.productInCart.forEach(e => {
          total += e.price * this.cart.find(x => x.productDetailId === e.id).quantity
        })
        return total
      } else {
        return 0
      }
    },
    genTotalMoneyNet() {
      if (this.cart.length > 0) {
        let total = 0
        this.productInCart.forEach(e => {
          total += e.priceNet * this.cart.find(x => x.productDetailId === e.id).quantity
        })
        return total
      } else {
        return 0
      }
    },
    payment() {
      if (this.orderW.paymentType === 'PAYMENT_ONLINE') {
        paymentOnline({
          id: this.userId,
          money: this.genTotalMoneyNet(),
          bankCode: '',
          billMobile: this.orderW.phoneNumber,
          billEmail: this.orderW.email,
          fullname: this.orderW.fullName,
          billAddress: this.orderW.address,
          billCity: '',
          billCountry: '',
          billState: ''
        }).then(res => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            window.location.replace(res.data)
          }
        })
      } else {
        orderOnline(this.orderW).then(res => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            this.clearCart()
            this.$notify({
              title: 'Success',
              message: 'Đặt hàng thành công.',
              type: 'success'
            })
            this.$router.push({
              path: '/shop'
            })
          }
        })
      }
    },
    changePay() {
      this.setPay({
        city: this.city,
        district: this.district,
        ward: this.ward,
        paymentType: this.typePay,
        fullNameCustomer: this.fullNameCustomer,
        phoneNumber: this.phoneNumber,
        address: this.address,
        voucherId: this.voucherId,
        noteByCustomer: this.noteByCustomer,
        totalAmount: this.genTotalMoney(),
        createdBy: this.allInfor.id,
        orderDetails: this.cart,
        email: this.email
      })
    },
    async setListOrderDetail() {
      await this.getListProductDetail()
      this.productInCart.forEach(e => {
        this.orderW.listOrderDetail.push({
          id: null,
          orderId: null,
          productDetail: null,
          productDetailId: e.id,
          productDetailPrice: e.price,
          productDetailPriceNet: e.priceNet,
          quantity: this.cart.find(x => x.productDetailId === e.id).quantity,
          totalAmount: this.genTotalMoneyProduct(e.id, e.price),
          totalAmountNet: this.genTotalMoneyProduct(e.id, e.priceNet),
          createdAt: '',
          updatedAt: '',
          createdBy: '',
          updatedBy: '',
          status: Status.ACTIVE
        })
      })
      this.orderW.totalAmount = this.genTotalMoney()
      this.orderW.totalAmountNet = this.genTotalMoneyNet()
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
