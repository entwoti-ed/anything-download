<!-- author: CY -->
<template>
    <div id="downloadList">
        <div v-if="downloadList.length !== 0">
            <section :key="index" v-for="(item, index) in downloadList">
                <DownloadItem :class="index === downloadList.length - 1 ? 'border-bottom' : ''" :item="item"
                              class="border-top"/>
            </section>
        </div>
        <div class="d-flex flex-column justify-content-center align-items-center text-muted" style="height: 50vh;"
             v-else>
            <font-awesome-icon icon="inbox" size="7x"/>
            <span>空下载列表...</span>
        </div>
    </div>
</template>

<script>

    import DownloadItem from "@/components/DownloadItem";
    import {mapState} from "vuex";

    export default {
        name: "DownloadList",

        created() {
            // 根据路由的参数来显示不同的列表
            this.active = this.$route.params.active;
        },
        data() {
            return {
                active: 'downloading',
            }
        },
        computed: {
            downloadList() {
                let downloadList = [];
                if (this.active === 'downloading') {
                    downloadList = this.downloadingList;
                } else if (this.active === 'finished') {
                    downloadList = this.finishedList;
                }
                return downloadList;
            },
            ...mapState([
                "downloadingList",
                "finishedList"
            ])
        },
        components: {
            DownloadItem
        }
    }
</script>

<style scoped>

</style>