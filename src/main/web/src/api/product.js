import request from '@/utils/request'

export const productCreateOrUpdate = (data) =>
  request({
    url: '/product',
    method: 'post',
    data
  })
