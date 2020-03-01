import '@babel/polyfill'
import 'mutationobserver-shim'
import './plugins/fontawesome'
import './plugins/bootstrap-vue'
import './plugins/logger'
import Vue from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
