/**
 * PornHub下载脚本
 */
export default () => {
    try { /*获取window对象里面的flashvars_xxxx变量里面的mediaDefinitions*/
        let videos = window[Object.keys(window).filter(x => x.includes("flashvars"))[0]]["mediaDefinitions"];
        /*获取最高质量*/
        let highQuality = Math.max.apply(null, videos.map(x => x["quality"]).flat());
        if (highQuality > 1080) highQuality = 1080;
        /*获取最高质量的视频*/
        let highQualityVideos = videos.filter(x => Number(x["quality"]) === highQuality);
        let videoUrlArr = "";
        if (highQualityVideos.length === 2) {
            /*如果最高质量有两种格式, 过滤出mp4的格式的URL对象*/
            videoUrlArr = highQualityVideos.filter(x => x["format"] === "mp4");
        }
        let videoUrlObj = videoUrlArr[0];
        let serviceType = "NORMAL";
        if (videoUrlObj["format"] === "hls") {
            serviceType = "HLS";
        }
        let videoUrl = videoUrlObj["videoUrl"];
        let title = encodeURI(document.title.replace(" - Pornhub.com", "") + ".mp4");
        let source = "PornHub";
        let cookie = document.cookie;
        let downloadType = "VIDEO";
        window.open(`http://127.0.0.1:8080/#/?serviceType=${btoa(serviceType)}&url=${btoa(videoUrl)}&title=${btoa(title)}&source=${btoa(source)}&cookie=${btoa(cookie)}&downloadType=${btoa(downloadType)}`);
    } catch (e) {
        alert("当前页面不支持使用该插件!");
    }
}