module.exports = {
    /** 区分生产环境与开发环境
     * process.env.NODE_ENV==='production' (生产环境)
     * process.env.NODE_ENV==='development' (开发环境)
     * baseUrl: process.env.NODE_ENV==='production'?"https://www.weiziplus.com/test/":'test/',
     */
    //打包时注意路径问题
    publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
    // 运行时生成的生产环境构建文件的目录(默认''dist''，构建之前会被清除)
    outputDir: process.env.outputDir,
    //放置生成的静态资源(s、css、img、fonts)的(相对于 outputDir 的)目录(默认'')
    assetsDir: 'assets',
    //指定生成的 index.html 的输出路径(相对于 outputDir)也可以是一个绝对路径
    indexPath: 'index.html',
    // 是否在构建生产包时生成 sourceMap 文件，false将提高构建速度// 。
    productionSourceMap: false,
    devServer: {// 环境配置
        host: 'localhost',
        port: 8088,
        https: false,
        hotOnly: false
    }
};