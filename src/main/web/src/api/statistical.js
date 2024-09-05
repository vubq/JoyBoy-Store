import request from '@/utils/request'

export function getTotalProductsSold(params) {
  return request({
    url: '/statistical/get-total-products-sold',
    method: 'get',
    params
  })
}

export function getTotalRevenueProducts(params) {
  return request({
    url: '/statistical/get-total-revenue-products',
    method: 'get',
    params
  })
}

export function getTop10ProductBetSelling(params) {
  return request({
    url: '/statistical/top-10-product-bet-selling',
    method: 'get',
    params
  })
}
