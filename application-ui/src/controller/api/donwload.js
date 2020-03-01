import {BASE_URL} from "@/controller/const/base-data";

/**
 * 自定义请求工具
 */
let cFetch = (uri, query, callback) => {
    fetch(BASE_URL + uri, {
        method: "POST",
        headers: new Headers({
            "Content-Type": "application/json;charset=UTF-8"
        }),
        body: JSON.stringify(query)
    }).then(x => x.json()).then(callback)
};

export default {

    /**
     * 获取下载列表API
     */
    downloadList(query, callback) {
        cFetch("/download/downloadList", query, callback);
    },

    /**
     * 开始下载API
     * @param query
     * @param callback
     */
    startDownload(query, callback) {
        cFetch("/download/startDownload", query, callback);
    },

    /**
     * 开始下载API
     * @param query
     * @param callback
     */
    normalDownload(query, callback) {
        cFetch("/download/normalDownload", query, callback);
    }
}