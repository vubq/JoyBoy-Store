<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Danh sách Mã giảm giá</h4>
        <span style="font-size: 14px;">Quản lý Mã giảm giá</span>
      </div>

      <el-button type="primary" icon="el-icon-circle-plus-outline" style="margin-left: 10px;" @click="dialogFormVisible = true">
        Thêm mới
      </el-button>
    </div>

    <div class="container-form-table">
      <div>
        <el-form @submit.native.prevent>
          <el-form-item>
            <el-input
              v-model="listQuery.filter"
              placeholder="Tìm kiếm"
              style="width: 200px"
              @keyup.enter.native="getList()"
            >
              <i slot="suffix" class="el-icon-search" style="cursor: pointer;" @click="getList()" />
            </el-input>

            <el-select
              v-model="listQuery.status"
              style="margin-left: 10px;"
              placeholder=""
              @change="getList()"
            >
              <el-option
                v-for="s in listStatus"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select>

            <!-- <el-button style="margin-left: 10px;" @click="getList()">
              Tìm kiếm
              <i class="el-icon-search" />
            </el-button> -->

            <!-- <el-button :loading="downloadLoading" type="primary" style="margin-left: 10px;" @click="exportFileExcel()">
              Xuất excel
              <i class="el-icon-news" />
            </el-button> -->
          </el-form-item>
        </el-form>
      </div>

      <el-divider />

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        style="width: 100%;"
        :default-sort="sortDefault"
        @sort-change="sortChange"
      >
        <el-table-column label="Stt" align="center" width="100">
          <template slot-scope="scope">
            <span>#{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Mã" prop="code" sortable="custom" align="center" width="200" />

        <el-table-column label="Loại giảm giá" prop="description" sortable="custom" align="center" width="150">
          <template slot-scope="{row}">
            <span v-if="row.type === VoucherType.PERCENT">
              <el-tag type="primary">Phần trăm</el-tag>
            </span>
            <span v-if="row.type === VoucherType.MONEY">
              <el-tag type="primary">Tiền</el-tag>
            </span>
          </template>
        </el-table-column>

        <el-table-column label="Giá trị" prop="value" sortable="custom" align="center" width="200">
          <template slot-scope="{row}">
            <span v-if="row.type === VoucherType.PERCENT">
              {{ row.value }}% (Tối đa: {{ formatCurrencyVND(row.max) }})
            </span>
            <span v-if="row.type === VoucherType.MONEY">
              {{ formatCurrencyVND(row.value) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="Thời gian bắt đầu" prop="createdAt" sortable="custom" align="center" width="200">
          <template slot-scope="{row}">
            <span>{{ moment(row.startDate).format('HH:mm:ss DD-MM-YYYY') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Thời gian kết thúc" prop="createdAt" sortable="custom" align="center" width="200">
          <template slot-scope="{row}">
            <span>{{ moment(row.endDate).format('HH:mm:ss DD-MM-YYYY') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Thời gian tạo" prop="createdAt" sortable="custom" align="center" width="200">
          <template slot-scope="{row}">
            <span>{{ moment(row.createdAt).format('HH:mm:ss DD-MM-YYYY') }}</span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="Trạng thái" prop="status" align="center" width="150">
          <template slot-scope="{row}">
            <span v-if="row.status === Status.ACTIVE">
              <el-tag type="success" size="small">Hoạt động</el-tag>
            </span>
            <span v-if="row.status === Status.IN_ACTIVE">
              <el-tag type="danger" size="small">Kết thúc</el-tag>
            </span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="" align="center" width="150">
          <template slot-scope="{row}">
            <el-button type="primary" size="small" plain @click="openModalEdit(row.id)">
              <i class="el-icon-edit" />
            </el-button>

            <el-button type="danger" size="small" plain @click="openConfirmDelete(row.id)">
              <i class="el-icon-delete" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.perPage" @pagination="getList" />

      <el-dialog
        width="30%"
        :title="voucher.id ? 'Chỉnh sửa' : 'Thêm mới'"
        :visible.sync="dialogFormVisible"
        :close-on-click-modal="false"
        @closed="closeModal()"
      >
        <el-form
          ref="dataForm"
          :model="voucher"
          label-position="left"
          :v-model="voucher"
          :rules="rules"
        >
          <el-form-item v-if="voucher.id" label="Mã" prop="code">
            <el-input v-model="voucher.code" :disabled="true" />
          </el-form-item>

          <el-form-item label="Loại giảm giá" prop="type">
            <el-select v-model="voucher.type" style="width: 100%;" placeholder="">
              <el-option
                v-for="t in listVoucherType"
                v-if="t.value !== 'ALL'"
                :key="t.value"
                :label="t.label"
                :value="t.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Giá trị" prop="value">
            <el-input v-model="voucher.value" />
          </el-form-item>

          <el-form-item v-if="voucher.type === VoucherType.PERCENT" label="Tối đa" prop="value">
            <el-input v-model="voucher.max" />
          </el-form-item>

          <el-form-item label="Số lượng" prop="quantity">
            <el-input v-model="voucher.quantity" />
          </el-form-item>

          <el-form-item label="Thời gian" prop="startDate">
            <el-date-picker
              v-model="voucherDate"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="~"
              start-placeholder="Từ ngày"
              end-placeholder="Tới ngày"
              :clearable="false"
              format="dd-MM-yyyy"
              value-format="yyyy-MM-dd"
              style="width: 100%;"
              @change="changeVoucherDate()"
            />
          </el-form-item>

          <el-form-item label="Trạng thái" prop="status">
            <el-select v-model="voucher.status" style="width: 100%;" placeholder="">
              <el-option
                v-for="s in listStatus"
                v-if="s.value !== 'ALL'"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item v-if="voucher.id && voucher.createdAt && voucher.createdBy" style="margin-bottom: 0;">
            <span>Thời gian tạo: {{ moment(voucher.createdAt).format('HH:mm:ss DD-MM-YYYY') }} (bởi: {{ voucher.createdBy }})</span>
          </el-form-item>

          <el-form-item v-if="voucher.id && voucher.updatedAt && voucher.updatedBy" style="margin-bottom: 0;">
            <span>Thời gian cập nhật gần nhất: {{ moment(voucher.updatedAt).format('HH:mm:ss DD-MM-YYYY') }} (bởi: {{ voucher.updatedBy }})</span>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="closeModal()">
            Hủy
          </el-button>
          <el-button type="primary" @click="edit()">
            {{ voucher.id ? 'Chỉnh sửa' : 'Thêm mới' }}
          </el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { voucherGetAllPage, voucherGetById, voucherCreateOrUpdate } from '@/api/voucher'
import Pagination from '@/components/Pagination'
import { ResponseCode, Status, VoucherType } from '@/enums/enums'
import { parseTime } from '@/utils'
import { formatCurrencyVND } from '@/utils/format'
import moment from 'moment'

export default {
  name: 'ProductManagementVoucherListPage',
  components: { Pagination },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('Không được để trống'))
      }
      if (this.voucher.type === VoucherType.PERCENT && value > 50) {
        return callback(new Error('Không được quá 50%'))
      }
      callback()
    }
    return {
      moment: moment,
      Status: Status,
      formatCurrencyVND: formatCurrencyVND,
      VoucherType: VoucherType,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        currentPage: 1,
        perPage: 10,
        filter: '',
        sortBy: '',
        sortDesc: '',
        status: Status.ALL
      },
      sortDefault: {
        prop: 'createdAt',
        order: 'descending'
      },
      listStatus: [
        { value: Status.ALL, label: 'Chọn Trạng thái' },
        { value: Status.ACTIVE, label: 'Hoạt động' },
        { value: Status.IN_ACTIVE, label: 'Kết thúc' }
      ],
      listVoucherType: [
        { value: Status.ALL, label: 'Chọn Loại mã giảm giá' },
        { value: VoucherType.MONEY, label: 'Tiền' },
        { value: VoucherType.PERCENT, label: 'Phần trăm' }
      ],
      dialogFormVisible: false,
      downloadLoading: false,
      voucher: {
        id: null,
        code: null,
        type: VoucherType.MONEY,
        value: 0,
        quantity: 0,
        max: 0,
        startDate: null,
        endDate: null,
        createdAt: null,
        updatedAt: null,
        createdBy: null,
        updatedBy: null,
        status: Status.ACTIVE
      },
      voucherDate: [],
      rules: {
        type: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ],
        value: [
          { validator: validateUsername, trigger: 'blur' }
        ],
        quantity: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ],
        max: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
  },
  created() {
    this.listQuery.sortBy = this.sortDefault.prop
    this.listQuery.sortDesc = this.sortDefault.order === 'descending'
    this.getList()
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    getList() {
      this.listLoading = true
      voucherGetAllPage(this.listQuery).then(res => {
        setTimeout(() => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            this.listLoading = false
            this.list = res.items
            this.total = res.totalRows
          }
        }, 500)
      })
    },
    sortChange(sortChange) {
      this.listQuery.sortBy = sortChange.prop
      this.listQuery.sortDesc = sortChange.order === 'descending'
      this.getList()
    },
    openModalEdit(id) {
      voucherGetById(id).then(res => {
        this.dialogFormVisible = true
        this.voucher = res.data
        this.voucherDate = [
          moment(res.data.startDate).format('YYYY-MM-DD'),
          moment(res.data.endDate).format('YYYY-MM-DD')
        ]
      })
    },
    closeModal() {
      this.dialogFormVisible = false
      this.$refs['dataForm'].clearValidate()
      this.voucher = {
        id: null,
        code: null,
        type: VoucherType.MONEY,
        value: 0,
        quantity: 0,
        max: 0,
        startDate: null,
        endDate: null,
        createdAt: null,
        updatedAt: null,
        createdBy: null,
        updatedBy: null,
        status: Status.ACTIVE
      }
      this.voucherDate = []
    },
    edit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.voucher.id) {
            this.$confirm('Bạn xác nhận cập nhật?', 'Xác nhận cập nhật', {
              confirmButtonText: 'Xác nhận',
              cancelButtonText: 'Hủy',
              type: 'warning'
            }).then(() => {
              voucherCreateOrUpdate(this.voucher).then(res => {
                if (res && res.code === ResponseCode.CODE_SUCCESS) {
                  this.$message({
                    showClose: true,
                    message: 'Cập nhật thành công!',
                    type: 'success'
                  })
                  this.dialogFormVisible = false
                  this.getList()
                }
              })
            })
          } else {
            this.$confirm('Bạn xác nhận thêm mới?', 'Xác nhận thêm mới', {
              confirmButtonText: 'Xác nhận',
              cancelButtonText: 'Hủy',
              type: 'warning'
            }).then(() => {
              voucherCreateOrUpdate(this.voucher).then(res => {
                if (res && res.code === ResponseCode.CODE_SUCCESS) {
                  this.$message({
                    showClose: true,
                    message: 'Thêm mới thành công!',
                    type: 'success'
                  })
                  this.dialogFormVisible = false
                  this.getList()
                }
              })
            })
          }
        } else {
          return false
        }
      })
    },
    openConfirmDelete(id) {
      voucherGetById(id).then(res1 => {
        if (res1 && res1.code === ResponseCode.CODE_SUCCESS) {
          this.$confirm('Chuyển trạng thái thành: "Kết thúc", bạn chắc chắn chứ?', 'Xác nhận xóa', {
            confirmButtonText: 'Xác nhận',
            cancelButtonText: 'Hủy',
            type: 'warning'
          }).then(() => {
            res1.data.status = Status.IN_ACTIVE
            voucherCreateOrUpdate(res1.data).then(res2 => {
              if (res2 && res2.code === ResponseCode.CODE_SUCCESS) {
                this.$message({
                  showClose: true,
                  message: 'Cập nhật thành công!',
                  type: 'success'
                })
                this.getList()
              }
            })
          })
        }
      })
    },
    exportFileExcel() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['name', 'description', 'createdAt', 'status']
        const filterVal = ['name', 'description', 'createdAt', 'status']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    changeVoucherDate() {
      this.voucher.startDate = new Date(this.voucherDate[0] + ' 00:00:00.000')
      this.voucher.endDate = new Date(this.voucherDate[1] + ' 23:59:59.999')
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
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
