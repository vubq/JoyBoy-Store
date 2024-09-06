<template>
  <div class="container-web">
    <el-row :gutter="200">
      <el-col :span="12">
        <div style="padding: 25% 0 25% 12%;">
          <img style="filter: brightness(0) invert(1);" :src="require('@/assets/images/logojb.png')" alt="">
        </div>
      </el-col>

      <el-col :span="12">
        <div class="login-form">
          <h3>Đăng ký</h3>

          <el-form ref="dataFormLogin" v-loading="loading" :model="user" :rules="rules">
            <el-form-item v-if="isShowSuccess">
              <el-alert
                title="Đăng ký thành công!"
                type="success"
                show-icon
                :closable="false"
              />
            </el-form-item>

            <el-form-item label="Tài khoản" prop="userName">
              <el-input v-model="user.userName" placeholder="Tài khoản" />
            </el-form-item>

            <el-form-item label="Mật khẩu" prop="password">
              <el-input v-model="user.password" type="password" placeholder="Mật khẩu" />
            </el-form-item>

            <el-form-item label="Nhập lại mật khẩu" prop="passwordConfirm">
              <el-input v-model="user.passwordConfirm" type="password" placeholder="Nhập lại mật khẩu" />
            </el-form-item>

            <el-form-item label="Họ tên" prop="fullName">
              <el-input v-model="user.fullName" placeholder="Họ tên" />
            </el-form-item>

            <el-form-item label="Email" prop="email">
              <el-input v-model="user.email" placeholder="Email" />
            </el-form-item>

            <el-form-item label="Số điện thoại" prop="phoneNumber">
              <el-input v-model="user.phoneNumber" placeholder="Số điện thoại" />
            </el-form-item>

            <el-form-item label="Tỉnh/Thành phố" prop="cityCode">
              <el-select v-model="user.cityCode" filterable style="width: 100%;">
                <el-option
                  v-for="c in listCity"
                  :key="c.province_id"
                  :label="c.province_name"
                  :value="c.province_id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Quận/Huyện" prop="districtCode">
              <el-select v-model="user.districtCode" filterable style="width: 100%;">
                <el-option
                  v-for="d in listDistrict"
                  :key="d.district_id"
                  :label="d.district_name"
                  :value="d.district_id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Phường/Xã" prop="wardCode">
              <el-select v-model="user.wardCode" filterable style="width: 100%;">
                <el-option
                  v-for="w in listWard"
                  :key="w.ward_id"
                  :label="w.ward_name"
                  :value="w.ward_id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Địa chỉ">
              <el-input
                v-model="user.address"
                type="textarea"
                :rows="3"
              />
            </el-form-item>

            <el-form-item style="margin-bottom: 0;">
              <el-button style="width: 100%" type="primary" @click="register">Đăng ký</el-button>
            </el-form-item>

            <el-divider content-position="center" />

            <el-form-item>
              <div style="font-size: 15px; color: rgba(0,0,0,.26); text-align: center;">Bạn đã có tài khoản? <span style="color: #11A983; font-weight: bold;">Đăng nhập</span></div>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'
import { registerUser } from '@/api/auth'
import { ResponseCode } from '@/enums/enums'

export default {
  name: 'LoginPage',
  components: { },
  data() {
    return {
      user: {
        username: '',
        password: ''
      },
      rules: {
        userName: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        passwordConfirm: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        fullName: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        phoneNumber: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        cityCode: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        districtCode: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        wardCode: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ]
      },
      isShowError: false,
      isShowSuccess: false,
      otherQuery: {},
      redirect: undefined,
      loading: false,
      listCity: [],
      listDistrict: [],
      listWard: []
    }
  },
  computed: {
    ...mapState({
      roles: state => state.user.roles
    })
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    },
    'user.cityCode': function() {
      axios.get('https://vapi.vnappmob.com/api/province/district/' + this.user.cityCode)
        .then(res => {
          if (res.data) {
            this.listDistrict = res.data.results
          }
        })
      this.user.city = this.listCity.find(e => e.province_id === this.user.cityCode).province_name
    },
    'user.districtCode': function() {
      axios.get('https://vapi.vnappmob.com/api/province/ward/' + this.user.districtCode)
        .then(res => {
          if (res.data) {
            this.listWard = res.data.results
          }
        })
      this.user.district = this.listDistrict.find(e => e.district_id === this.user.districtCode).district_name
    },
    'user.wardCode': function() {
      this.user.ward = this.listWard.find(e => e.ward_id === this.user.wardCode).ward_name
    }
  },
  created() {
    axios.get('https://vapi.vnappmob.com/api/province/')
      .then(res => {
        if (res.data) {
          this.listCity = res.data.results
        }
      })
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    async register() {
      this.$refs.dataFormLogin.validate(async valid => {
        if (valid) {
          this.isShowSuccess = false
          this.loading = true
          registerUser(this.user).then((res) => {
            if (res && res.code === ResponseCode.CODE_SUCCESS) {
              this.isShowSuccess = true
              this.loading = false
              console.log('aaaa')
            }
          })
          // .finally(() => this.loading = false)
        } else {
          return false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
  }
}
</script>

<style lang="scss" scoped>
.container-web {
  background-color: #11A983;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .08);
  border-radius: 4px;
  padding: 40px;
}

.login-form {
  background-color: #ffffff;
  border-radius: 4px;
  padding: 10px 30px 20px 30px;
}
</style>
