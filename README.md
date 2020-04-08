<div style="margin: 0 auto;"><img src="images/标题.png"/></div>

---

该工具还处于草稿阶段...

## 工具介绍

这个工具专门用来下载各网站上面的视频，使用起来就像下面动画一样

![](/images/使用方法.gif)

当前仅支持`Windows`

已经支持`BiliBili`，`Tencent`，`Youku`，`Pornhub`，很快支持`icourse163`，`imooc`等平台...

如果你想要支持你想要的平台，可以自己添加`support`模块，也可以添加脚本，或者`issues`

软件运行环境`JDK8+`

## 使用方法

如果你是Java程序员，或者会使用Java语言，那么启动该程序一定很轻松...

```
java -jar xxx.jar
```

如果你不是程序员，那么我不推荐你使用...

将首页上面的脚本拖拽到书签栏，然后在想要下载的页面点击书签，就可以下载了。

具体使用方法[点击查看]()

## 吐槽

下载工具使用了`Aria2c`和`FFMpeg`，灵感来源于两个仓库[`Bilibili-Evolved`](https://github.com/the1812/Bilibili-Evolved)和[`N_m3u8DL-CLI_Core`](https://github.com/nilaoda/N_m3u8DL-CLI_Core)

平时自己也总喜欢将资源保存到电脑上，写了不少零碎的`Demo`，甚至还为了练习`JavaFX`，写了[这个](https://halo.cyblogs.top/release/%E6%85%95%E8%AF%BE%E7%BD%91%E4%B8%8B%E8%BD%BD%E5%99%A8.exe)软件。为了方便各位程序员获取一些学习资料，所以想了想，为什么不把那些`Demo`全部汇聚到一起，形成一个项目，并且提供脚本的形式，和后台完成对接，从而实现多平台使用... 想法总是不错的，实现起来很费劲，因为各个网站都不一样啊，有的加密了，有的没有加密，有的直接就可以获取到下载连接，有的并不行，总之网站太多，各不相同...

[为什么我不全部使用`js`脚本？](https://halo.cyblogs.top/archives/2020-why-not-develop-broswer-script)

## 加入开发

现在暂时不推荐加入开发，因为代码还处于草稿期，很乱!

- 打包方式：双击项目目录下的`package.cmd`

- 运行方式：打包完成在目录下执行：

```shell
java -jar ./application/target/download.jar
```

- 默认端口号为`10086`，可以[直接访问](http://localhost:10086)，如果不能访问，当然是你没启动程序...

默认的路径在用户目录下的`Downloads`文件夹里面，暂时还不支持更换...
