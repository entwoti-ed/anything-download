<template>
    <div id="downloadList">
        <section :key="index"
                 v-for="(item, index) in downloadList">
            <DownloadItem :class="index === downloadList.length - 1 ? 'border-bottom' : ''"
                          :item="item"
                          class="border-top"/>
        </section>

        <RightBottom/>

    </div>
</template>

<script>

    import DownloadItem from "@/components/DownloadItem";
    import RightBottom from "@/components/RightBottom";

    export default {
        name: "DownloadList",
        props: {
            downloadList: {
                type: [Object],
                required: true
            }
        },
        components: {
            DownloadItem,
            RightBottom
        }
    }
</script>

<style scoped>

</style>