<!-- eslint-disable vue/valid-v-on -->
<!-- eslint-disable vue/no-unused-vars -->
<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="app-container">
    <div class="container-form-header">
      <div style="display: flex; justify-content: center; align-items: center;">
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Thống kê</h4>
      </div>

      <el-date-picker
        v-model="orderDate"
        type="daterange"
        align="right"
        unlink-panels
        range-separator="~"
        start-placeholder="Từ ngày"
        end-placeholder="Tới ngày"
        :clearable="false"
        format="dd-MM-yyyy"
        value-format="yyyy-MM-dd"
        style="width: 300px; margin-left: 10px;"
        @change="changeOrderDate()"
      />
    </div>

    <el-row :gutter="40" style="margin-top: 40px; margin-bottom: 40px;">
      <el-col :span="8">
        <div style="background-color: #34bfa3; color: #ffffff; padding: 20px; display: flex; justify-content: space-between; align-items: center;">
          <i class="el-icon-shopping-cart-full" style="font-size: 40px;" />
          <div />
          <div style="text-align: end;">
            <div style="font-weight: 700;">Tổng sản phẩm bán được</div>
            <div style="font-size: 17px;">{{ totalProductsSold }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="background-color: #f4516c; color: #ffffff; padding: 20px; display: flex; justify-content: space-between; align-items: center;">
          <i class="el-icon-money" style="font-size: 40px;" />
          <div />
          <div style="text-align: end;">
            <div style="font-weight: 700;">Tổng doanh thu</div>
            <div style="font-size: 17px;">{{ formatCurrencyVND(Number(totalRevenueProducts)) }}</div>
          </div>
        </div>
      </el-col>
      <!-- <el-col :span="8">
        <div style="background-color: #ffffff;">x</div>
      </el-col> -->
    </el-row>

    <div class="container-form-table" style="padding-bottom: 20px;">
      <div style="padding-bottom: 20px;">TOP 10 sản phẩm bán chạy</div>

      <el-table
        :key="0"
        :data="top10Product"
        style="width: 100%;"
        border
      >
        <el-table-column label="Sản phẩm" prop="productName">
          <template slot-scope="{row}">
            <router-link class="link" :to="'/admin/product-management/product/detail?id=' + row.id">{{ row.productName }}</router-link>
          </template>
        </el-table-column>

        <el-table-column label="Số lượng bán được" prop="quantity" />

        <el-table-column label="Tổng doanh thu">
          <template slot-scope="{row}">
            <span>{{ formatCurrencyVND(Number(row.totalMoney)) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import { formatCurrencyVND } from '@/utils/format'
import { getTotalProductsSold, getTotalRevenueProducts, getTop10ProductBetSelling } from '@/api/statistical'

export default {
  name: 'ProductManagementBrandListPage',
  components: {},
  data() {
    return {
      formatCurrencyVND: formatCurrencyVND,
      orderDate: [],
      dateFrom: moment(new Date()).subtract(1, 'month').format('YYYY-MM-DD'),
      dateTo: moment(new Date()).format('YYYY-MM-DD'),
      totalProductsSold: 0,
      totalRevenueProducts: 0,
      top10Product: []
    }
  },
  watch: {},
  created() {
    this.orderDate[0] = this.dateFrom
    this.orderDate[1] = this.dateTo

    this.init()
  },
  mounted() {},
  destroyed() {},
  methods: {
    init() {
      getTotalProductsSold({
        dateFrom: this.orderDate[0],
        dateTo: this.orderDate[1]
      }).then(res => {
        console.log(res)
        this.totalProductsSold = res.data
      })

      getTotalRevenueProducts({
        dateFrom: this.orderDate[0],
        dateTo: this.orderDate[1]
      }).then(res => {
        console.log(res)
        this.totalRevenueProducts = res.data
      })

      getTop10ProductBetSelling({
        dateFrom: this.orderDate[0],
        dateTo: this.orderDate[1]
      }).then(res => {
        console.log(res)
        this.top10Product = res.data
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

::v-deep .input-quantity {
  input {
    padding-left: 1px; padding-right: 1px;
  }
}

::v-deep .tableOPD {
  .el-table--medium th, .el-table--medium td {
    height: 80px;
  }
}
.link:hover {
  text-decoration: underline;
}
</style>
