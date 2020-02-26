<template>

  <div class="container py-4" id="app">

    <!--程序标题-->
    <GlobalTitle/>

    <div class="row mt-3">

      <!--左侧区域-->
      <div class="col-lg-3 col-md-0" id="left">
        <!--导航列表-->
        <NavList :download-count="downloadCount"/>
        <!--全局下载状态-->
        <GlobalStatus class="mt-4"/>
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
        <router-view/>
      </div>
    </div>

    <!--页脚信息-->
    <footer class="mt-5 text-center">
      <a class="text-muted" href="https://halo.cyblogs.top">
        CY'Blogs
      </a>
      <router-link class="text-muted ml-3"
                   exact replace tag="a"
                   to="/moreScript">
        更多脚本
      </router-link>
    </footer>
  </div>
</template>

<script>
  import GlobalTitle from "@/components/GlobalTitle";
  import NavList from "@/components/NavList";
  import GlobalStatus from "@/components/GlobalStatus";
  import MobileNavList from "@/components/MobileNavList";

  import downloadListener from "@/controller/websocket/download-listener"

  export default {
    created() {
      this.renderDownloadList();
    },
    data() {
      return {
        downloadList: null,
        downloadCount: {
          downloadingCount: 0,
          finishedCount: 0,
          trashCount: 0
        }
      };
    },
    components: {
      NavList,
      GlobalTitle,
      GlobalStatus,
      MobileNavList
    },
    methods: {
      /**
       * 渲染下载列表
       */
      renderDownloadList() {
        downloadListener((downloadList) => {
          this.downloadList = downloadList;
        });
      }
    }
  }
</script>


<style>
  /* 禁止用户在界面上进行选择 */
  * {
    user-select: none;
  }

  ::-webkit-scrollbar {
    width: 0.375rem;
    height: 0.25rem;
    background-color: rgba(0, 0, 0, 0);
  }

  /* 滚动条颜色 */
  ::-webkit-scrollbar-thumb {
    background-color: #007bff !important;
  }

  ::-webkit-scrollbar-track {
    background-color: rgba(0, 0, 0, .125);
  }

  @media (max-width: 992px) {
    #left {
      display: none !important;
    }
  }
</style>
