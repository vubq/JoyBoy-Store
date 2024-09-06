import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import getPageTitle from '@/utils/get-page-title'
import { Role } from './enums/enums'

NProgress.configure({ showSpinner: false })

const whiteList = ['/admin/login', '/auth-redirect', '/shop/login', '/shop/register', '/shop/home', '/shop/product/:id']

router.beforeEach(async(to, from, next) => {
  NProgress.start()
  document.title = getPageTitle(to.meta.title)
  const hasToken = getToken()

  if (hasToken) {
    console.log(store.getters.roles)
    if (to.path === '/admin/login' && !store.getters.roles.includes(Role.ROLE_CUSTOMER)) {
      next({ path: '/' })
      NProgress.done()
    } else if (to.path === '/shop/login' && store.getters.roles.includes(Role.ROLE_CUSTOMER)) {
      next({ path: '/shop' })
      NProgress.done()
    } else if (to.path.includes('/shop') && !store.getters.roles.includes(Role.ROLE_CUSTOMER)) {
      await store.dispatch('user/resetToken')
      next({ path: to.path })
    } else if (to.path.includes('/admin') && store.getters.roles.includes(Role.ROLE_CUSTOMER)) {
      await store.dispatch('user/resetToken')
      next({ path: to.path })
    } else {
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      const { roles } = await store.dispatch('user/getInfo')
      const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
      if (hasRoles) {
        next()
      } else {
        try {
          router.addRoutes(accessRoutes)
          next({ ...to, replace: true })
        } catch (error) {
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else if (to.path.includes('/shop/product')) {
      next()
    } else {
      if (to.path.includes('shop')) {
        next(`/shop/login?redirect=${to.path}`)
      } else {
        next(`/admin/login?redirect=${to.path}`)
      }
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
