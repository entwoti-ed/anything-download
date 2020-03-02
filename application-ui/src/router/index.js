import Vue from 'vue'
import VueRouter from 'vue-router'
import BasicLayout from "@/views/BasicLayout";

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        component: BasicLayout,
        redirect: "/start",
        children: [
            {
                path: 'start',
                name: '首页',
                component: () => import(/* webpackChunkName: "home" */'@/views/Home')
            },
            {
                path: 'list/:active',
                name: '下载列表',
                component: () => import(/* webpackChunkName: "downloadList" */'@/views/DownloadList')
            },
            {
                path: 'settings',
                name: '设置',
                component: () => import(/* webpackChunkName: "settings" */'@/views/Settings.vue')
            },
            {
                path: 'moreScript',
                name: '更多脚本',
                component: () => import(/* webpackChunkName: "scriptList" */'@/views/ScriptList.vue')
            }
        ]
    },

];

const router = new VueRouter({
    routes
});

export default router
