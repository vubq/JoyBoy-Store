import request from '@/utils/request'

export const orderPaySalesAtTheCounter = (data) =>
  request({
    url: '/order/pay-sales-at-the-counter',
    method: 'post',
    data
  })

export const createOrder = (data) =>
  request({
    url: '/order/create-order',
    method: 'post',
    data
  })

export const orderOnline = (data) =>
  request({
    url: '/order/order-online',
    method: 'post',
    data
  })

export const orderGetAllPageOnline = (params) =>
  request({
    url: '/order/get-all-page-online',
    method: 'get',
    params
  })

export const orderGetDetailById = (id) =>
  request({
    url: '/order/get-detail-by-id/' + id,
    method: 'get'
  })

export const orderChangeStatus = (data) =>
  request({
    url: '/order/change-status',
    method: 'post',
    data
  })

export const orderGetAllByUser = () =>
  request({
    url: '/order/get-list-order-by-user',
    method: 'get'
  })
