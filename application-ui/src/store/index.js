import Vue from 'vue'
import Vuex from 'vuex'
import downloadListener from "@/controller/websocket/download-listener"

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    /*下载中*/
    downloadingList: [],
    /*已完成*/
    finishedList: [],
    /*垃圾桶*/
    trashList: []
  },
  mutations: {
    /**
     * 刷新下载列表
     */
    refreshDownloadList(state, downloadList) {
      let downloadingList = [];
      let finishedList = [];
      let trashList = [];
      downloadList.forEach(x => {
        if (x.status === "FINISHED") {
          finishedList.push(x);
        } else if (x.status === "WAITING" || x.status === "DOWNLOADING" || x.status === "MERGING") {
          downloadingList.push(x);
        } else if (x.status === "TRASH") {
          trashList.push(x);
        }
      });
      state.downloadingList = downloadingList;
      state.finishedList = finishedList;
      state.trashList = trashList;
    }
  },
  actions: {
    /**
     * 下载列表监听
     * @param commit
     */
    downloadListAction({commit}) {
      downloadListener((downloadList) => {
        console.dir(JSON.stringify(downloadList));
        // 提交刷新
        commit("refreshDownloadList", downloadList)
      });
    }
  },
  modules: {},
  getters: {}
})
