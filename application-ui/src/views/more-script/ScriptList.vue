<template>
    <div class="row no-gutters" id="scriptList">
        <div :key="index"
             class="col-3 border rounded py-4 d-flex flex-column justify-content-center align-items-center"
             v-for="(item, index) in scriptList">
            <i :class="item.icon" class="script-icon"/>
            <a :href="item.script" class="mt-3 text-muted">{{item.name}}</a>
        </div>
    </div>
</template>

<script>

    // 更多脚本列表
    import moreScript from './more'
    // 脚本包装工具
    import {wrapLinkScript} from '@/utils/script-utils'

    export default {
        name: "ScriptList",
        data() {
            return {
                scriptList: [{
                    name: "PornHub",
                    script: wrapLinkScript(moreScript.pornHub),
                    icon: "porn-hub-icon"
                }]
            };
        }
    }
</script>

<style scoped>
    .script-icon {
        width: 6rem;
        height: 6rem;
        display: block;
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
    }

    .porn-hub-icon {
        background-image: url("~@/assets/logo/pornhub.png");
    }
</style>