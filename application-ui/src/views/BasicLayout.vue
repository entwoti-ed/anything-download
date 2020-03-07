<!-- author: CY -->
<template>
    <div class="container">

        <!--程序标题-->
        <div class="d-flex justify-content-center align-items-center py-4">
            <img alt="Logo" src="@/assets/logo.png" style="width: 50%; height: auto;"/>
        </div>

        <div class="row">
            <!--左侧区域-->
            <div class="col-lg-3 col-md-0" id="left">
                <!--导航列表-->
                <NavList/>
            </div>
            <!--右侧区域-->
            <div class="col-lg-9 col-md-12" id="right">
                <!--移动端的导航栏-->
                <MobileNavList class="mb-3"/>
                <!--警告信息-->
                <b-alert dismissible show="" variant="danger">
                    <strong>Hey man!</strong> 合理合法使用下载后的资源...
                </b-alert>
                <!--切换页面-->
                <router-view :key="key"/>
            </div>
        </div>

        <!--页脚信息-->
        <footer class="mt-5 text-center">
            <a class="text-muted" href="https://halo.cyblogs.top" title="访问我的博客" v-b-tooltip.hover>
                CY'Blogs
            </a>
            <router-link class="text-muted ml-3" exact replace
                         tag="a" title="更多下载脚本" to="/moreScript"
                         v-b-tooltip.hover>
                更多脚本
            </router-link>
        </footer>
    </div>
</template>

<script>

    import NavList from "@/components/NavList";
    import MobileNavList from "@/components/MobileNavList";
    import {mapActions} from "vuex";

    export default {
        created() {
            /**
             * 程序加载就启动下载列表监听
             */
            this.downloadListAction();
        },
        computed: {
            /**
             * 为了保证每次都重新加载组件，不用默认的组件复用
             * @return {*}
             */
            key() {
                return this.$route.path + "?d=" + Date.now();
            }
        },
        components: {
            NavList,
            MobileNavList
        },
        methods: {
            ...mapActions([
                "downloadListAction"
            ])
        }
    }
</script>

<style scoped>
    @media (max-width: 992px) {
        #left {
            display: none !important;
        }
    }
</style>