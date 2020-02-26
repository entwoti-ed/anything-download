/**
 * PornHub下载脚本
 */
export default () => {
    let videos = window[Object.keys(window).filter(x => x.includes("flashvars"))[0]]["mediaDefinitions"];
    let highQuality = Math.max.apply(null, videos.map(x => x["quality"]).flat());
    let highQualityVideos = videos.filter(x => Number(x["quality"]) === highQuality);
    let videoUrl = "";
    if (highQualityVideos.length === 2) {
        videoUrl = highQualityVideos.filter(x => x["format"] === "mp4")[0]["videoUrl"];
    } else {
        videoUrl = highQualityVideos[0]["videoUrl"];
    }
    console.dir(videoUrl);
}