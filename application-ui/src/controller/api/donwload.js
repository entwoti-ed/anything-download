import {BASE_URL} from "@/controller/const/base-data";

let cFetch = (url, query, callback) => {
    fetch(BASE_URL + url, {
        method: "POST",
        headers: new Headers({
            "Content-Type": "application/json;charset=UTF-8"
        }),
        body: JSON.stringify(query)
    }).then(x => x.json()).then(callback)
};

export default {
    addDownload(query, callback) {
        cFetch("/download/addDownload", query, callback);
    }
}