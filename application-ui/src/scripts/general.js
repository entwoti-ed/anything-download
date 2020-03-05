export default () => {
    /*URL Query*/
    let query = "?" + Object.entries({
        /*获取当前页面URL*/
        url: window.location.href,
        /*获取当前页面中的Cookie*/
        cookie: document.cookie
    }).map(x => {
        return x[0] + "=" + encodeURIComponent(btoa(encodeURIComponent(x[1])));
    }).join("&");

    /*传送到Anything Download*/
    window.open(`http://127.0.0.1:10086/#/${query}`);
}