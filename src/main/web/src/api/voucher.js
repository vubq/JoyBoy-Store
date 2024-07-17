import request from '@/utils/request'

export const voucherGetAllPage = (params) =>
  request({
    url: '/voucher/get-all-page',
    method: 'get',
    params
  })

export const voucherGetById = (id) =>
  request({
    url: '/voucher/' + id,
    method: 'get'
  })

export const voucherCreateOrUpdate = (data) =>
  request({
    url: '/voucher',
    method: 'post',
    data
  })
