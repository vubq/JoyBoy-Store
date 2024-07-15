import request from '@/utils/request'

export const sizeGetAllPage = (params) =>
  request({
    url: '/size/get-all-page',
    method: 'get',
    params
  })

export const sizeGetById = (id) =>
  request({
    url: '/size/' + id,
    method: 'get'
  })

export const sizeCreateOrUpdate = (data) =>
  request({
    url: '/size',
    method: 'post',
    data
  })
