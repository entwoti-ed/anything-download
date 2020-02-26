<template>
    <div id="home">
        <!-- 轮播图 -->
        <CarouselImage/>

        <div class="my-3">粘贴链接到下面, 然后点击下载按钮</div>

        <b-input-group>
            <b-form-input placeholder="粘贴URL到这里" type="text"/>
            <b-input-group-append>
                <b-button variant="outline-secondary">下载</b-button>
            </b-input-group-append>
        </b-input-group>

        <div class="text-center mt-3">
            <a :href="generalScript"
               class="d-inline text-muted"
               id="bookMark">
                通用脚本
            </a>
        </div>

        <!--交互弹窗等内容-->
        <div class="cover-screen d-flex flex-column justify-content-center align-items-center"
             v-if="currentState === 'waiting'">
            <b-spinner label="Spinning" style="width: 120px; height: 120px;" type="grow" variant="danger"/>
            <span class="text-white mt-4">正在请求中, 请稍等...</span>
        </div>


        <div class="cover-screen d-flex flex-column justify-content-center align-items-center"
             v-if="currentState === 'select'">
            <div class="bg-white py-4 px-4 rounded" style="width: 30rem;">
                <div class="pb-3 border-bottom">
                    <span class="font-weight-bold">请选择</span>
                </div>
                <div class="mt-3 d-flex align-items-center">
                    <font-awesome-icon class="mr-3" icon="download"/>
                    <div class="font-weight-bold">下载列表</div>
                    <b-badge class="ml-auto">全选</b-badge>
                    <b-badge class="ml-2">反选</b-badge>
                    <span class="ml-2 text-muted">12/12</span>
                </div>
                <div class="mt-4" style="max-height: 16rem; overflow-y: auto;">
                    <b-form-checkbox class="mt-1" v-model="selectDownload" value="id">
                        <p class="text-ellipsis" style="max-width: 25rem;">
                            为什么大象的鼻子会喷水...为什么大象的鼻子会喷水..为什么大象的鼻子会喷水..为什么大象的鼻子会喷水..为什么大象的鼻子会喷水..为什么大象的鼻子会喷水..</p>
                    </b-form-checkbox>
                </div>
                <div class="mt-4 text-right">
                    <b-button @click="closeMessage" size="sm" variant="danger">取消</b-button>
                    <b-button class="ml-2" size="sm" variant="success">开始</b-button>
                </div>
            </div>
        </div>


        <div class="cover-screen d-flex flex-column justify-content-center align-items-center"
             v-if="currentState === 'error'">
            <div class="bg-white py-4 px-4 rounded" style="width: 30rem;">
                <div class="border-bottom pb-3">
                    <span class="font-weight-bold">出错了...</span>
                </div>
                <div class="mt-4">
                    <span>{{errorInfo}}</span>
                </div>
                <div class="text-right mt-4">
                    <b-button @click="closeMessage" class="mr-3" href="https://www.baidu.com" target="_blank"
                              variant="warning">支持列表
                    </b-button>
                    <b-button @click="closeMessage" variant="info">知道了</b-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

    import CarouselImage from "@/components/CarouselImages";
    // 脚本包装工具
    import {wrapLinkScript} from '@/utils/script-utils'
    import api from '@/controller/api/donwload'

    // TODO 修改生产环境
    let toDownloadPage = () => {
        let href = btoa(window.location.href);
        let cookie = btoa(document.cookie);
        window.open(`http://127.0.0.1:8080/#/?url=${href}&cookie=${cookie}`);
    };

    export default {
        name: 'Home',
        components: {
            CarouselImage
        },
        created() {
            this.initialDownload();
        },
        data() {
            return {
                generalScript: wrapLinkScript(toDownloadPage),
                currentState: null,
                errorInfo: null,
            }
        },
        methods: {
            initialDownload() {
                let query = this.$route.query;
                let keys = Object.keys(query);
                if (!keys.length) return;
                this.currentState = 'waiting';
                keys.forEach(x => {
                    query[x] = atob(query[x]);
                });
                api.addDownload(query, response => {
                    if (response.flag) {
                        // 下载列表
                    } else {
                        this.errorInfo = response.message;
                        this.currentState = "error";
                    }

                })
            },
            closeMessage() {
                this.currentState = null;
                this.errorInfo = null
            }
        }
    }
</script>

<style scoped>
    .cover-screen {
        position: fixed;
        z-index: 9999999;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        background-color: rgba(0, 0, 0, 0.6);
    }

    /* 下载标题 */
    .text-ellipsis {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    @media (max-width: 768px) {
        #bookMark {
            display: none !important;
        }
    }
</style>
