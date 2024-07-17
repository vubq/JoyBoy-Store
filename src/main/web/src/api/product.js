import request from '@/utils/request'

export const productCreateOrUpdate = (data) =>
  request({
    url: '/product',
    method: 'post',
    data
  })

export const productGetById = (id) =>
  request({
    url: '/product/' + id,
    method: 'get'
  })

export const productGetAllPage = (params) =>
  request({
    url: '/product/get-all-page',
    method: 'get',
    params
  })

export const productUpdateStatusInActiveById = (id) =>
  request({
    url: '/product/update-status-in-active-by-id/' + id,
    method: 'post'
  })
