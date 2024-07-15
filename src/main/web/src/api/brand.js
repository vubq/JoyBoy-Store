import request from '@/utils/request'

export const brandGetAllPage = (params) =>
  request({
    url: '/brand/get-all-page',
    method: 'get',
    params
  })

export const brandGetById = (id) =>
  request({
    url: '/brand/' + id,
    method: 'get'
  })

export const brandCreateOrUpdate = (data) =>
  request({
    url: '/brand',
    method: 'post',
    data
  })
