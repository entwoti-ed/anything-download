/*这里千万不要用一些ES2015的新特性，尽量用老一点的语法，否则会被Webpack包装导致不能使用*/
import general from "@/scripts/general";
import pornHub from "@/scripts/pornhub";
import tencentVideo from "@/scripts/tencent-video";
import youkuVideo from "@/scripts/youku-video";
// 脚本包装工具
import wrapLinkScript from '@/utils/script-utils';

export default {
    general: wrapLinkScript(general),
    pornHub: wrapLinkScript(pornHub),
    tencentVideo: wrapLinkScript(tencentVideo),
    youkuVideo: wrapLinkScript(youkuVideo),
}