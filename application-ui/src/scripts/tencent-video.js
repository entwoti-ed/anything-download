export default () => {
    try {
        let dataset = window.PLAYER._DownloadMonitor.context.dataset;
        /*URL Query*/
        let query = "?" + Object.entries({
            serviceType: "HLS",
            url: dataset.playList[0].url,
            title: dataset.title + ".mp4",
            source: "腾讯视频",
            cookie: document.cookie,
            downloadType: "VIDEO"
        }).map(x => {
            return x[0] + "=" + encodeURIComponent(btoa(encodeURIComponent(x[1])));
        }).join("&");

        window.open(`http://127.0.0.1:10086/#/${query}`);
    } catch (e) {
        console.error(e);
        alert("当前页面不支持使用该插件!");
    }
}