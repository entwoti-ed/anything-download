/*VUE的全局配置*/
module.exports = {
    pages: {
        index: {
            // page 的入口
            entry: 'src/main.js',
            // 当使用 title 选项时，
            // template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
            title: 'Download Anything',
        }
    },
    // 通过npm run build命令打包的目标路径
    outputDir: "../application/src/main/resources/static/"
};