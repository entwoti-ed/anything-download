<template>
    <div id="home">

        <div class="d-flex flex-column justify-content-center align-items-center" style="height: 50vh;">

            <font-awesome-icon class="text-muted" icon="download" size="7x"/>

            <div class="mt-5 my-3">粘贴链接到下面, 然后点击下载按钮</div>

            <b-input-group>
                <b-form-input placeholder="粘贴URL到这里" type="text"/>
                <b-input-group-append>
                    <b-button variant="outline-secondary">下载</b-button>
                </b-input-group-append>
            </b-input-group>

            <div class="text-center mt-3">
                <a :href="generalScript" class="d-inline text-muted" id="bookMark">
                    通用脚本
                </a>
            </div>

        </div>

        <!--交互弹窗等内容-->
        <div class="cover-screen d-flex flex-column justify-content-center align-items-center"
             v-if="currentState === 'waiting'">
            <b-spinner label="Spinning" style="width: 120px; height: 120px;" type="grow" variant="danger"/>
            <span class="text-white mt-4">正在解析中, 请稍等...</span>
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
                    <b-badge @click="selectAll" class="ml-auto">全选</b-badge>
                    <b-badge @click="selectOther" class="ml-2">反选</b-badge>
                    <span class="ml-2 text-muted">{{selectDownload.length}}/{{tempDownloadList.length}}</span>
                </div>
                <div class="mt-4" style="max-height: 16rem; overflow-y: auto;">
                    <b-form-checkbox :key="index" :value="item.downloadId" class="mt-1"
                                     v-for="(item, index) in tempDownloadList" v-model="selectDownload">
                        <p class="text-ellipsis" style="max-width: 25rem;">
                            {{item.fileName}}
                        </p>
                    </b-form-checkbox>
                </div>
                <div class="mt-4 text-right">
                    <b-button @click="closeMessage" size="sm" variant="danger">取消</b-button>
                    <b-button @click="startDownload" class="ml-2" size="sm" variant="success">开始</b-button>
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

        <div class="cover-screen d-flex flex-column justify-content-center align-items-center"
             v-if="currentState === 'login'">
            <div class="bg-white py-4 px-4 rounded" style="width: 30rem;">
                <div class="border-bottom pb-3">
                    <span class="font-weight-bold">需要登录</span>
                </div>
                <div class="mt-4">
                    <b-form-group label="请粘贴Cookie" label-for="paste">
                        <b-form-input id="paste" required type="text" v-model="cookie"/>
                    </b-form-group>
                </div>
                <div class="text-right mt-4">
                    <b-button @click="initialDownload" variant="info">好了</b-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

    import api from '@/controller/api/donwload'

    import scriptList from '../scripts'

    export default {
        name: 'Home',
        components: {},
        created() {
            this.initialDownload();
        },
        data() {
            return {
                url: null,
                generalScript: scriptList.general,
                selectDownload: [],
                tempDownloadList: [],
                singleItem: null,
                currentState: null,
                errorInfo: null,
                cookie: null
            }
        },
        methods: {
            /**
             * 进入首页检查路由中是否带有参数
             * 进行Base64解码
             * 然后传递到后台进行解析下载
             * 解析成功后前台显示下载列表
             * 用户选择后进行下载
             */
            initialDownload() {
                let query = this.$route.query;
                if (this.cookie) {
                    query["cookie"] = this.cookie;
                    this.cookie = null;
                } else {
                    let keys = Object.keys(query);
                    if (!keys.length) return;
                    keys.forEach(x => {
                        query[x] = decodeURIComponent(query[x]);
                    });
                }
                this.showWaiting();
                if (query["serviceType"]) {
                    this.showSelectList([{
                        fileName: query["title"],
                        downloadId: "singleItem"
                    }]);
                    this.singleItem = query;
                } else {
                    api.downloadList(query, response => {
                        if (response.flag) {
                            this.showSelectList(response.data);
                        } else {
                            if (response.status === "FORBIDDEN") {
                                this.showLogin();
                            } else {
                                this.showError(response.message);
                            }
                        }
                    });
                }
            },
            showSelectList(selectList) {
                this.tempDownloadList = selectList;
                this.selectAll();
                this.currentState = 'select';
            },
            showError(message) {
                this.errorInfo = message;
                this.currentState = "error";
            },
            showWaiting() {
                this.currentState = 'waiting';
            },
            showLogin() {
                this.currentState = 'login';
            },
            closeMessage() {
                this.selectDownload = [];
                this.tempDownloadList = [];
                this.currentState = null;
                this.errorInfo = null;
                this.singleItem = null;
                let loc = window.location;
                let url = loc.protocol + "//" + loc.host + loc.pathname + "#/start";
                window.history.replaceState(null, null, url);
            },
            selectAll() {
                this.selectDownload = this.tempDownloadList.map(x => x.downloadId);
            },
            selectOther() {
                this.selectDownload = this.tempDownloadList.map(x => x.downloadId).filter(x => this.selectDownload.indexOf(x) === -1);
            },
            startDownload() {
                if (!this.selectDownload.length) {
                    this.showError("你啥都没选...");
                    return;
                }
                if (this.singleItem !== null) {
                    api.normalDownload(this.singleItem, response => {
                        if (response.flag) {
                            this.closeMessage();
                            this.$router.replace({
                                path: `/list/downloading`
                            });
                        } else {
                            this.showError(response.message);
                        }
                    });
                } else {
                    api.startDownload(this.selectDownload, response => {
                        if (response.flag) {
                            this.closeMessage();
                            this.$router.replace({
                                path: `/list/downloading`
                            })
                        } else {
                            this.showError(response.message);
                        }
                    });
                }
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
