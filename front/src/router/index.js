import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Main',
      component: Main
    }, {
      path: '/stores/:storeName',
      name: 'stores',
      component: Main
    }, {
      path: '/topics/:topicName',
      name: 'topics',
      component: Main
    }
  ]
})
