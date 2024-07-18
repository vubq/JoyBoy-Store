<!-- eslint-disable vue/valid-v-on -->
<!-- eslint-disable vue/no-unused-vars -->
<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Bán hàng tại quầy</h4>
        <span style="font-size: 14px;">Quản lý hóa đơn treo</span>
      </div>

      <el-button type="primary" icon="el-icon-circle-plus-outline" style="margin-left: 10px;" @click="addOrder(orderId)">
        Thêm hóa đơn
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="16">
        <div class="container-form-table" style="padding-top: 10px;">
          <div>
            <el-form>
              <el-form-item>
                <el-tabs v-model="orderId" closable @tab-remove="deleteOrder">
                  <el-tab-pane
                    v-for="(o, i) in listOrder"
                    :key="o.id"
                    :label="o.fullName ? o.fullName : ('Hóa đơn ' + o.id)"
                    :name="o.id"
                  >
                    <!-- <el-input
                      placeholder="Tìm kiếm Sản phẩm"
                      style="width: 100%"
                      @keyup.enter.native="getList()"
                    >
                      <i slot="suffix" class="el-icon-search" style="cursor: pointer;" @click="getList()" />
                    </el-input> -->

                    <el-autocomplete
                      v-model="searchProductDetail"
                      class="inline-input"
                      style="width: 100%;"
                      placeholder="Tìm kiếm Sản phẩm"
                      :fetch-suggestions="loadListProductDetail"
                      :trigger-on-focus="true"
                      @select="$item => selectProductDetail($item, i)"
                    >
                      <i slot="suffix" class="el-icon-search" style="cursor: pointer;" />
                      <template slot-scope="{ item }">
                        <el-divider v-if="item.index !== 0" class="divider-custom" />
                        <div style="margin-top: 20px; margin-bottom: 20px; display: flex;">
                          <el-image
                            style="width: 100px; height: 100px"
                            :src="item.listImage.length > 0 ? item.listImage[0] : item.product.listImage[0]"
                          />
                          <div style="margin-left: 20px;">
                            <div style="width: 100%;">
                              <span style="font-weight: bold;">{{ item.product.name }}</span>
                            </div>
                            <div style="width: 100%;">
                              <span>Thuộc tính: {{ item.size.name }} - {{ item.color.name }} - {{ item.material.name }}</span>
                            </div>
                            <div style="width: 100%;">
                              <span style="color: red;">Giá: {{ formatCurrencyVND(item.priceNet) }} <span style="text-decoration: line-through;">({{ formatCurrencyVND(item.price) }})</span></span>
                            </div>
                          </div>
                        </div>
                        <el-divider v-if="item.index === item.listSize" class="divider-custom" />
                      </template>
                    </el-autocomplete>

                    <el-table
                      :key="tableKey"
                      :data="o.listOrderDetail"
                      style="width: 100%;"
                    >
                      <el-table-column label="Stt" align="center" width="50">
                        <template slot-scope="scope">
                          <span>#{{ scope.$index + 1 }}</span>
                        </template>
                      </el-table-column>

                      <el-table-column label="Sản phẩm" prop="productDetail.product.name" align="center" />

                      <el-table-column label="Thuộc tính" align="center">
                        <template slot-scope="{row}">
                          {{ row.productDetail.size.name + ' - ' + row.productDetail.color.name + ' - ' + row.productDetail.material.name }}
                        </template>
                      </el-table-column>

                      <el-table-column label="Đơn giá" prop="productDetailPrice" align="center">
                        <template slot-scope="{row}">
                          {{ formatCurrencyVND(row.productDetailPrice) }}
                        </template>
                      </el-table-column>

                      <el-table-column fixed="right" label="Số lượng" prop="quantity" align="center">
                        <template slot-scope="{row}">
                          <div style="display: flex;">
                            <el-button
                              plain
                              type="info"
                              style="padding-left: 4px; padding-right: 4px;"
                              @click="--row.quantity"
                            >
                              <i class="el-icon-minus" />
                            </el-button>
                            <el-input
                              v-model="row.quantity"
                              style="text-align: center !important; margin-left: 4px; margin-right: 4px;"
                              class="input-text-align-center"
                            />
                            <el-button
                              plain
                              type="info"
                              style="padding-left: 4px; padding-right: 4px;"
                              @click="++row.quantity"
                            >
                              <i class="el-icon-plus" />
                            </el-button>
                          </div>
                        </template>
                      </el-table-column>

                      <el-table-column fixed="right" label="Tổng tiền gốc" prop="totalAmount" align="center">
                        <template slot-scope="{row}">
                          {{ formatCurrencyVND(row.totalAmount) }}
                        </template>
                      </el-table-column>

                      <el-table-column fixed="right" label="Tổng tiền giảm giá" prop="totalAmountNet" align="center">
                        <template slot-scope="{row}">
                          {{ formatCurrencyVND(row.totalAmountNet) }}
                        </template>
                      </el-table-column>

                      <el-table-column fixed="right" label="" align="center" width="80">
                        <template slot-scope="scope">
                          <el-button type="danger" size="small" plain @click="listOrder[i].listOrderDetail.splice(scope.$index, 1)">
                            <i class="el-icon-delete" />
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-tab-pane>
                </el-tabs>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="container-form-table">
          <div>
            <span style="font-size: 14px; font-weight: bold;">Thông tin Khách hàng</span>

            <el-form style="margin-top: 10px;" @submit.native.prevent>
              <el-form-item>
                <el-input
                  v-model="listOrder[indexOrder()].fullName"
                  placeholder="Tên Khách hàng"
                  class="input-border-bottom"
                  style="width: 100%"
                >
                  <i slot="suffix" class="el-icon-user" />
                </el-input>
              </el-form-item>

              <el-form-item>
                <el-input
                  v-model="listOrder[indexOrder()].phoneNumber"
                  placeholder="SĐT Khách hàng"
                  class="input-border-bottom"
                  style="width: 100%"
                >
                  <i slot="suffix" class="el-icon-phone-outline" />
                </el-input>
              </el-form-item>

              <el-form-item>
                <el-input
                  v-model="listOrder[indexOrder()].address"
                  placeholder="Địa chỉ Khách hàng"
                  class="input-border-bottom"
                  style="width: 100%"
                >
                  <i slot="suffix" class="el-icon-location-outline" />
                </el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="container-form-table">
          <div>
            <el-form @submit.native.prevent>
              <el-form-item>
                <label>Thông tin thanh toán</label>
              </el-form-item>

              <el-form-item>
                <div>
                  <label>Tổng tiền ({{ totalProductDetail() }} sản phẩm)</label>

                  <span style="float: right;">{{ formatCurrencyVND(listOrder[indexOrder()].totalAmount) }}</span>
                </div>
              </el-form-item>

              <el-form-item>
                <div>
                  <el-checkbox v-model="listOrder[indexOrder()].isVoucher">
                    <template #default>
                      <span style="font-weight: bold;">Mã giảm giá</span>
                    </template>
                  </el-checkbox>

                  <div v-if="listOrder[indexOrder()].isVoucher" style="float: right;">

                    <el-autocomplete
                      v-model="listOrder[indexOrder()].voucherCode"
                      class="inline-input input-border-bottom"
                      style="width: 100%;"
                      placeholder="Mã giảm giá"
                      :fetch-suggestions="loadListVoucher"
                      :trigger-on-focus="true"
                      @select="selectVoucher"
                    >
                      <i slot="suffix" class="el-icon-s-promotion" />
                      <template slot-scope="{ item }">
                        {{ item.code }}
                      </template>
                    </el-autocomplete>

                    <!-- <el-input v-model="listOrder[indexOrder()].voucherCode" class="input-border-bottom">
                      <i slot="suffix" class="el-icon-s-promotion" />
                    </el-input> -->
                  </div>
                </div>
              </el-form-item>

              <el-form-item>
                <div>
                  <label>Tổng tiền khách phải trả:</label>

                  <span style="float: right;">{{ formatCurrencyVND(listOrder[indexOrder()].totalAmountNet) }}</span>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import moment from 'moment'
import { formatCurrencyVND } from '@/utils/format'
import { productGetAllBySalesAtTheCounter } from '@/api/product'
import { ResponseCode, Status, VoucherType } from '@/enums/enums'
import { voucherGetAllLikeCodeAndStillActive, voucherGetByCode } from '@/api/voucher'

export default {
  name: 'ProductManagementBrandListPage',
  components: {},
  data() {
    return {
      tableKey: 0,
      moment: moment,
      formatCurrencyVND: formatCurrencyVND,
      listLoading: true,
      orderId: '1',
      listOrder: [
        {
          id: '1',
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
          paymentType: '',
          status: '',
          listOrderDetail: [],
          isVoucher: false,
          voucherCode: '',
          voucher: null
        }
      ],
      orderIndex: 1,
      searchProductDetail: ''
    }
  },
  watch: {
    listOrder: {
      deep: true,
      handler(newItems, oldItems) {
        for (let i = 0; i < this.listOrder.length; i++) {
          let totalAmountP = 0
          let totalAmountPNet = 0
          let totalDiscountMoney = 0
          let discountMoney = 0

          if (!this.listOrder[i].isVoucher) {
            this.listOrder[i].voucher = null
            this.listOrder[i].voucherCode = ''
          }

          if (this.listOrder[i].isVoucher && this.listOrder[i].voucherCode) {
            voucherGetByCode(this.listOrder[i].voucherCode)
              .then(res => {
                if (res && res.code === ResponseCode.CODE_SUCCESS && res.data) {
                  this.listOrder[i].voucher = res.data
                }
              })
          }

          if (this.listOrder[i].voucher && this.listOrder[i].voucher.type === VoucherType.MONEY) {
            totalDiscountMoney = this.listOrder[i].voucher.value
            discountMoney = this.listOrder[i].voucher.value / this.listOrder[i].listOrderDetail.length
          }

          if (this.listOrder[i].voucher && this.listOrder[i].voucher.type === VoucherType.PERCENT) {
            totalDiscountMoney = this.listOrder[i].voucher.value * this.listOrder[i].listOrderDetail.reduce((sum, od) => sum + (Number(od.quantity) * Number(od.productDetailPrice)), 0) / 100
            discountMoney = totalDiscountMoney / this.listOrder[i].listOrderDetail.length
          }

          for (let j = 0; j < this.listOrder[i].listOrderDetail.length; j++) {
            const totalAmountOD = this.listOrder[i].listOrderDetail[j].productDetailPrice * this.listOrder[i].listOrderDetail[j].quantity
            let totalAmountODNet = 0
            if (this.listOrder[i].isVoucher && this.listOrder[i].voucherCode && this.listOrder[i].voucher) {
              if (j === this.listOrder[i].listOrderDetail.length - 1) {
                totalAmountODNet = totalAmountP + totalAmountOD - totalDiscountMoney - totalAmountPNet
              } else {
                totalAmountODNet = Math.floor((this.listOrder[i].listOrderDetail[j].productDetailPrice * this.listOrder[i].listOrderDetail[j].quantity) - discountMoney)
              }
            } else {
              totalAmountODNet = this.listOrder[i].listOrderDetail[j].productDetailPrice * this.listOrder[i].listOrderDetail[j].quantity
            }
            this.listOrder[i].listOrderDetail[j].totalAmount = isNaN(totalAmountOD) ? 0 : totalAmountOD
            this.listOrder[i].listOrderDetail[j].totalAmountNet = isNaN(totalAmountODNet) ? 0 : totalAmountODNet
            totalAmountP += totalAmountOD
            totalAmountPNet += totalAmountODNet
          }

          this.listOrder[i].totalAmount = totalAmountP
          this.listOrder[i].totalAmountNet = totalAmountPNet
        }
      }
    }
  },
  created() {
    // console.log(this.listOrder.findIndex(o => o.id === this.orderId))
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    addOrder(o) {
      // eslint-disable-next-line prefer-const
      let newOrderId = ++this.orderIndex + ''
      this.listOrder.push({
        id: newOrderId,
        fullName: '',
        phoneNumber: '',
        email: '',
        address: '',
        city: '',
        district: '',
        ward: '',
        totalAmount: '',
        totalAmountNet: '',
        voucherId: '',
        noteByCustomer: '',
        noteByAdmin: '',
        createdAt: '',
        updatedAt: '',
        createdBy: '',
        updatedBy: '',
        completedAt: '',
        type: '',
        paymentType: '',
        status: '',
        listOrderDetail: [],
        isVoucher: false,
        voucherCode: '',
        voucher: null
      })
      this.orderId = newOrderId
    },
    deleteOrder(o) {
      // eslint-disable-next-line prefer-const
      let os = this.listOrder
      let orderIdActive = this.orderId
      if (orderIdActive === o) {
        os.forEach((tab, index) => {
          if (tab.id === o) {
            // eslint-disable-next-line prefer-const
            let nextTab = os[index + 1] || os[index - 1]
            if (nextTab) {
              orderIdActive = nextTab.id
            }
          }
        })
      }

      this.orderId = orderIdActive
      this.listOrder = os.filter(tab => tab.id !== o)
      if (this.listOrder.length === 0) {
        this.addOrder(this.orderId)
      }
    },
    indexOrder() {
      return this.listOrder.findIndex(o => o.id === this.orderId)
    },
    totalProductDetail() {
      if (this.listOrder[this.indexOrder()].listOrderDetail.length > 0) {
        const total = this.listOrder[this.indexOrder()].listOrderDetail.reduce((sum, od) => sum + Number(od.quantity), 0)
        return isNaN(total) ? 0 : total
      }
      return 0
    },
    selectProductDetail(item, i) {
      // console.log(i)
      // console.log(item)
      this.listOrder[i].listOrderDetail.push({
        id: null,
        orderId: null,
        productDetail: item,
        productDetailId: item.id,
        productDetailPrice: item.priceNet,
        quantity: 1,
        totalAmount: item.priceNet * 1,
        totalAmountNet: item.priceNet * 1,
        createdAt: '',
        updatedAt: '',
        createdBy: '',
        updatedBy: '',
        status: Status.ACTIVE
      })
    },
    selectVoucher(item) {
      this.listOrder[this.indexOrder()].voucherCode = item.code
    },
    loadListVoucher(code, cb) {
      voucherGetAllLikeCodeAndStillActive({
        code: (code !== null && code !== undefined && code !== '') ? code : ''
      })
        .then(res => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            cb(res.data)
          } else {
            cb(null)
          }
        })
    },
    loadListProductDetail(searchBy, cb) {
      productGetAllBySalesAtTheCounter({
        searchBy: (searchBy !== null && searchBy !== undefined && searchBy !== '') ? searchBy : ''
      }).then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          if (res.data.length > 0) {
            for (var i = 0; i < res.data.length; i++) {
              res.data[i].index = i
              res.data[i].listSize = res.data.length
            }
            cb(res.data)
          } else {
            cb(null)
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-input__suffix {
  right: 15px;
}
.divider-custom {
  margin: 0;
}

::v-deep .input-border-bottom {
  input {
    border-radius: 0 !important;
    border-top: 0 !important;
    border-left: 0 !important;
    border-right: 0 !important;
    // background-color: #FAFBFE !important;
    padding-left: 10px !important;
    padding-right: 10px !important;
  }
}
::v-deep .input-text-align-center {
  input {
    text-align: center !important;
  }
}
</style>
