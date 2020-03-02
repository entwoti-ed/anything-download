<template>
    <div id="downloadList">
        <div v-if="downloadList.length !== 0">
            <section :key="index"
                     v-for="(item, index) in downloadList">
                <DownloadItem :class="index === downloadList.length - 1 ? 'border-bottom' : ''"
                              :item="item"
                              class="border-top"/>
            </section>
            <RightBottom/>
        </div>
        <div class="text-center text-muted" style="line-height: 58vh;" v-else>
            空下载列表...
        </div>

    </div>
</template>

<script>

    import DownloadItem from "@/components/DownloadItem";
    import RightBottom from "@/components/RightBottom";
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
                } else if (this.active === 'trash') {
                    downloadList = this.trashList;
                }
                return downloadList;
            },
            ...mapState([
                "downloadingList",
                "finishedList",
                "trashList"
            ])
        },
        components: {
            DownloadItem,
            RightBottom
        }
    }
</script>

<style scoped>

</style>