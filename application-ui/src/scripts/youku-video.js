export default () => {
    try {
        let playListData = window.videoPlayer.getData()._playlistData;
        let title = playListData.video.title;
        let stream = playListData.stream;
        let maxSize = Math.max.apply(null, stream.map(x => x.size));
        let video = stream.filter(x => x.size === maxSize)[0];
        /*URL Query*/
        let query = "?" + Object.entries({
            serviceType: "HLS",
            url: video.m3u8_url,
            title: title + ".mp4",
            source: "优酷视频",
            cookie: document.cookie,
            downloadType: "VIDEO"
        }).map(x => {
            return x[0] + "=" + encodeURIComponent(x[1]);
        }).join("&");

        window.open(`http://127.0.0.1:10086/#/${query}`);
    } catch (e) {
        console.error(e);
        alert("当前页面不支持使用该插件!");
    }
}