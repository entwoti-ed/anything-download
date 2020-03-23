export default () => {
    try {
        /*获取window对象里面的flashvars_xxxx变量里面的mediaDefinitions*/
        let videos = window[Object.keys(window).filter(x => x.includes("flashvars"))[0]]["mediaDefinitions"];
        /*获取最高质量*/
        let highQuality = Math.max.apply(null, videos.filter(x => x.videoUrl).map(x => x["quality"]).flat());
        /*获取最高质量的视频*/
        let hdVideos = videos.filter(x => Number(x["quality"]) === highQuality);
        /*如果视频数量多，则过滤出mp4，否则*/
        let videoUrlObj = hdVideos.length === 2 ? hdVideos.filter(x => x["format"] === "mp4")[0] : hdVideos[0];
        /*URL Query*/
        let query = "?" + Object.entries({
            serviceType: videoUrlObj["format"] === "hls" ? "HLS" : "NORMAL",
            url: videoUrlObj["videoUrl"],
            title: document.title.replace(" - Pornhub.com", "") + ".mp4",
            source: "PornHub",
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