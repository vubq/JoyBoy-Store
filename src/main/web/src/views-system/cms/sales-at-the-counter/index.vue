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
                    <el-input
                      placeholder="Tìm kiếm Sản phẩm"
                      style="width: 100%"
                      @keyup.enter.native="getList()"
                    >
                      <i slot="suffix" class="el-icon-search" style="cursor: pointer;" @click="getList()" />
                    </el-input>
                  </el-tab-pane>
                </el-tabs>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="container-form-table" style="padding-top: 10px;">
          <div>
            <el-form @submit.native.prevent>
              <el-form-item>
                vvv
              </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="container-form-table" style="padding-top: 10px;">
          <div>
            <el-form @submit.native.prevent>
              <el-form-item>
                ccc
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

export default {
  name: 'ProductManagementBrandListPage',
  components: {},
  data() {
    return {
      moment: moment,
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
          listOrderDetail: []
        }
      ],
      orderIndex: 1
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
        listOrderDetail: []
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
}
</style>
