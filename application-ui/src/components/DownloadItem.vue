<template>
    <!--一个下载项-->
    <section>

        <div class="d-flex">

            <!-- 下载类型图标 -->
            <div class="d-flex justify-content-center align-items-center border-right download-type">
                <font-awesome-icon class="text-danger" icon="file-video" size="3x"/>
            </div>

            <!-- 图标右边的一堆信息 -->
            <div class="d-flex flex-column flex-fill px-4 download-details"
                 style="justify-content: space-evenly;">

                <!-- 第一行信息 -->
                <section class="d-flex">
                    <span :title="downloadItem.fileName" class="font-weight-bold mr-auto download-title"
                          v-b-tooltip.hover>
                        {{downloadItem.fileName}}
                    </span>
                    <div class="text-muted download-status-lg">
                        {{downloadItem.statusFormat}}
                    </div>
                </section>

                <!-- 第二行信息 -->
                <section class="d-flex">
                    <div class="mr-auto text-muted">
                        大小: {{downloadItem.totalSize}}
                    </div>
                    <div class="text-muted download-status-sm">
                        {{downloadItem.statusFormat}}
                    </div>
                    <div :title="item.targetPath || downloadItem.targetPath" class="text-muted target-path"
                         v-b-tooltip.hover.bottom>
                        位置: {{downloadItem.targetPath}}
                    </div>
                </section>

                <!-- 第三行信息 -->
                <section class="d-flex">
                    <div class="mr-auto">
                        来源: <span class="text-danger">{{downloadItem.source}}</span>
                    </div>
                    <div class="text-muted">
                        {{downloadItem.currentSpeed}}
                    </div>
                    <div class="ml-2 text-muted">
                        {{downloadItem.progressFormat}}
                    </div>
                </section>
            </div>
        </div>
        <!-- 进度条 -->
        <div style="background-color: darkgrey">
            <div :class="downloadItem.status"
                 :style="`width: ${downloadItem.progress}%;`"
                 style="height: 3px;"></div>
        </div>
    </section>

</template>

<script>
    export default {
        name: "DownloadItem",
        props: {
            item: {
                type: [Object],
                required: true
            }
        },
        computed: {
            downloadItem() {
                return {
                    fileName: this.item.fileName || "文件名获取中...",
                    statusFormat: this.item.statusFormat || "正在连接...",
                    totalSize: this.item.totalSize || "--KB",
                    targetPath: (this.item.targetPath || "").replace(/(.{8})(.*)(.{12})/, "$1……$3") || "获取中...",
                    source: this.item.source || "获取中...",
                    currentSpeed: this.item.currentSpeed,
                    progressFormat: this.item.progressFormat,
                    progress: isNaN(this.item.progress) ? 0 : this.item.progress,
                    status: this.item.status === 'DOWNLOADING' ? 'bg-danger' : this.item.status === 'MERGING' ? 'bg-info' : this.item.status === 'FINISHED' ? 'bg-success' : '',

                };
            }
        }
    }
</script>

<style scoped>
    /* 下载类型图标 */
    .download-type {
        width: 6rem;
        height: 100px;
    }

    /* 下载标题 */
    .download-title {
        width: 22rem;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    /* 小屏下的下载状态 */
    .download-status-sm {
        display: none;
    }

    /* 小型电脑 */
    @media (max-width: 1200px) {
        .download-title {
            width: 18rem;
        }
    }

    /* 普通手机 */
    @media (max-width: 768px) {

        .download-title {
            width: 18rem;
        }

        .download-type {
            display: none !important;
        }

        .target-path {
            display: none !important;
        }

        .download-status-lg {
            display: none !important;
        }

        .download-status-sm {
            display: block !important;
        }

        .download-control {
            display: none !important;
        }

        .download-details {
            height: 100px;
            padding: 0 !important;
        }
    }
</style>