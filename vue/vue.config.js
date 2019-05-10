module.exports = {
    /** 区分打包环境与开发环境
     * process.env.NODE_ENV==='production' (打包环境)
     * process.env.NODE_ENV==='development' (开发环境)
     * baseUrl: process.env.NODE_ENV==='production'?"https://www.weiziplus.com/front/":'front/',
     */
    baseUrl: process.env.NODE_ENV === 'production' ? './' : '/', //打包时注意路径问题
    outputDir: 'dist',// 运行时生成的生产环境构建文件的目录(默认''dist''，构建之前会被清除)
    assetsDir: 'assets',//放置生成的静态资源(s、css、img、fonts)的(相对于 outputDir 的)目录(默认'')
    indexPath: 'index.html',//指定生成的 index.html 的输出路径(相对于 outputDir)也可以是一个绝对路径。
    devServer: {// 环境配置
        host: 'localhost',
        port: 8088,
        https: false,
        hotOnly: false
    }
};