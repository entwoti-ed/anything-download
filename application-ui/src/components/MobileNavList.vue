<template>
    <!--移动端的导航列表-->
    <section class="d-flex" id="navBar" style="justify-content: space-evenly;">
        <router-link class="py-1" exact
                     exact-active-class="m-nav-active"
                     replace
                     tag="div"
                     to="/start">
            主页
        </router-link>
        <router-link class="py-1" exact
                     exact-active-class="m-nav-active"
                     replace
                     tag="div"
                     to="/list/downloading">
            下载中
        </router-link>
        <router-link class="py-1" exact
                     exact-active-class="m-nav-active"
                     replace
                     tag="div"
                     to="/list/finished">
            已完成
        </router-link>
        <router-link class="py-1" exact
                     exact-active-class="m-nav-active"
                     replace
                     tag="div"
                     to="/list/trash">
            垃圾箱
        </router-link>
        <router-link class="py-1" exact
                     exact-active-class="m-nav-active"
                     replace
                     tag="div"
                     to="/settings">
            设置
        </router-link>
    </section>

</template>

<script>
    export default {
        name: "MobileNavList"
    }
</script>

<style scoped>
    .m-nav-active {
        border-bottom: 1px solid #007BFF;
    }

    #navBar {
        display: none !important;
    }

    /* 小型平板 */
    @media (max-width: 992px) {
        #navBar {
            display: flex !important;
        }
    }
</style>