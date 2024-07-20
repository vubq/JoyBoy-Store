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
