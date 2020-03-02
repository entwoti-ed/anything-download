<template>
    <div id="navList">

    <b-list-group>

            <!--首页-->
            <router-link class="d-flex align-items-center" exact
                         exact-active-class="list-group-item-secondary"
                         replace
                         tag="b-list-group-item"
                         to="/start">
                <font-awesome-icon class="mr-3" icon="home"/>
                <span>首页</span>
            </router-link>

            <!--下载中-->
        <router-link class="d-flex align-items-center" exact
                     exact-active-class="list-group-item-secondary"
                     replace
                     tag="b-list-group-item"
                     to="/list/downloading">
            <font-awesome-icon class="mr-3" icon="download"/>
            <span class="mr-1">下载中</span>
            <span class="small mr-auto">{{globalStatus}}</span>
            <b-badge pill variant="primary">{{downloadingList.length}}</b-badge>
        </router-link>

            <!--已完成-->
        <router-link class="d-flex align-items-center" exact
                     exact-active-class="list-group-item-secondary"
                     replace
                     tag="b-list-group-item"
                     to="/list/finished">
            <font-awesome-icon class="mr-3" icon="check-circle"/>
            <span class="mr-auto">已完成</span>
            <b-badge pill variant="primary">{{finishedList.length}}</b-badge>
        </router-link>

            <!--垃圾箱-->
        <router-link class="d-flex align-items-center" exact
                     exact-active-class="list-group-item-secondary"
                     replace
                     tag="b-list-group-item"
                     to="/list/trash">
            <font-awesome-icon class="mr-3" icon="trash"/>
            <span class="mr-auto">垃圾箱</span>
            <b-badge pill variant="primary">{{trashList.length}}</b-badge>
        </router-link>

            <!--设置-->
            <router-link class="d-flex align-items-center" exact
                         exact-active-class="list-group-item-secondary"
                         replace
                         tag="b-list-group-item"
                         to="/settings">
                <font-awesome-icon class="mr-3" icon="cog"/>
                <span>设置</span>
            </router-link>
        </b-list-group>
    </div>
</template>

<script>
    import {mapState} from "vuex"

    export default {
        name: "NavList",
        computed: {
            ...mapState([
                "downloadingList",
                "finishedList",
                "trashList",
                "globalStatus"
            ])
        }
    }
</script>

<style scoped>

</style>