<template>
    <!--首页的轮播图-->
    <div id="carouselImages">
        <b-carousel :interval="4000" controls indicators>
            <b-carousel-slide :img-alt="item.imageAlt"
                              :key="index"
                              :img-src="item.imageUrl"
                              v-for="(item, index) in carouselList"/>
        </b-carousel>
    </div>
</template>

<script>
    export default {
        name: "CarouselImages",
        data() {
            return {
                carouselList: [{
                    imageUrl: "http://p.qlogo.cn/qqmail_head/Q3auHgzwzM4g2cLj1J8wBePWc7IpPAic1zf6BFE19PfAw4iczE5vMANoGicPMwcDTwMt70J6IcTPJvDY7T18QIefzC7aMBbKibRQiaCED65jCgNY/0",
                    imageAlt: "慕课网下载器",
                }],
            }
        }
    }
</script>

<style scoped>

</style>