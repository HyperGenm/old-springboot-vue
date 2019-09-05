//当前是生产环境还是开发环境
const IS_Production = process.env.NODE_ENV === 'production';
const CompressionWebpackPlugin = require('compression-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

module.exports = {
    /** 区分生产环境与开发环境
     * process.env.NODE_ENV==='production' (生产环境)
     * process.env.NODE_ENV==='development' (开发环境)
     * publicPath: process.env.NODE_ENV==='production'?"https://www.weiziplus.com/test/":'test/',
     */
    //打包时注意路径问题
    publicPath: IS_Production ? './' : '/',
    // 运行时生成的生产环境构建文件的目录(默认''dist''，构建之前会被清除)
    outputDir: process.env.outputDir,
    //放置生成的静态资源(s、css、img、fonts)的(相对于 outputDir 的)目录(默认'')
    assetsDir: 'assets',
    //指定生成的 index.html 的输出路径(相对于 outputDir)也可以是一个绝对路径
    indexPath: 'index.html',
    // 是否在构建生产包时生成 sourceMap 文件，false将提高构建速度// 。
    productionSourceMap: false,
    css: {
        // 是否使用css分离插件 ExtractTextPlugin
        extract: false,
        // css预设器配置项
        loaderOptions: {},
        // 启用 CSS modules for all css / pre-processor files.
        modules: false
    },
    chainWebpack: config => {
        //如果是生产环境
        if (IS_Production) {
            // 移除 prefetch 插件
            config.plugins.delete('prefetch');
            // 移除 preload 插件
            config.plugins.delete('preload');
        }
    },
    configureWebpack: config => {
        //如果是生产环境
        if (IS_Production) {
            //打包去除的资源
            config.externals = {
                'vue': 'Vue',
                'vue-router': 'VueRouter',
                'vuex': 'Vuex',
                'axios': 'axios',
                'element-ui': 'ELEMENT',
                'wangeditor': 'E'
            };
            //开启Gzip
            config.plugins.push(new CompressionWebpackPlugin({
                algorithm: 'gzip',
                //匹配的文件名
                test: /\.js$|\.html$|\.css/,
                //对超过10K的压缩
                threshold: 10240,
                //是否删除原文件
                deleteOriginalAssets: false
            }));
            //查看打包后的文件信息
            config.plugins.push(new BundleAnalyzerPlugin(
                {
                    analyzerMode: 'server',
                    analyzerHost: 'localhost',
                    // 运行后的端口号
                    analyzerPort: 8000,
                    reportFilename: 'report.html',
                    defaultSizes: 'parsed',
                    //是否自动打开浏览器
                    openAnalyzer: true,
                    generateStatsFile: false,
                    statsFilename: 'stats.json',
                    statsOptions: null,
                    logLevel: 'info'
                }
            ));
            //去掉注释
            config.plugins.push(new UglifyJsPlugin({
                uglifyOptions: {
                    compress: {
                        drop_debugger: true,
                        drop_console: true,
                        // 移除console
                        pure_funcs: ['console.log', 'console.warn']
                    },
                },
                sourceMap: false,
                parallel: true,
            }));
        }
    },
    //本地项目运行时的环境配置
    devServer: {
        host: 'localhost',
        port: 80,
        https: false,
        hotOnly: false
    }
};