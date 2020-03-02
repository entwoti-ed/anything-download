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
    trashList: [],
    /*全局状态*/
    globalStatus: null,
  },
  mutations: {
    /**
     * 刷新下载列表
     */
    refreshDownloadList(state, data) {

      let downloadingList = [];
      let finishedList = [];
      let trashList = [];
      data.downloadList.forEach(item => {
        if (item.status === "FINISHED") {
          finishedList.push(item);
        } else if (item.status === "WAITING" || item.status === "DOWNLOADING" || item.status === "MERGING") {
          downloadingList.push(item);
        } else if (item.status === "TRASH") {
          trashList.push(item);
        }
      });
      state.downloadingList = downloadingList;
      state.finishedList = finishedList;
      state.trashList = trashList;
      // 全局下载速度
      state.globalStatus = data.globalSpeed;
    }
  },
  actions: {
    /**
     * 下载列表监听
     * @param commit
     */
    downloadListAction({commit}) {
      downloadListener((data) => {
        console.dir(JSON.stringify(data));
        // 提交刷新
        commit("refreshDownloadList", data);
      });
    }
  },
  modules: {},
  getters: {}
})
