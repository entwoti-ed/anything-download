/**
 * 包装一个函数，使其成为链接脚本
 * @param scriptFunction 脚本方法，不能出现注释也不能省略分号
 * @return {string}
 */
export function wrapLinkScript(scriptFunction) {
    return `javascript:void((${scriptFunction.toString().replace(/\n/g, '')})())`;
}