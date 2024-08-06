const HtmlWebpackPlugin = require("html-webpack-plugin");
const {CleanWebpackPlugin} = require("clean-webpack-plugin");
const CopyWebpackPlugin = require('copy-webpack-plugin');
const path = require("path");
const webpack = require("webpack");

const ASSET_PATH = process.env.ASSET_PATH || '/';

module.exports = (env, argv) => {
    const prod = argv.mode === "production";

    return {
        mode: prod ? "production" : "development",
        devtool: prod ? "hidden-source-map" : "eval",
        entry: {
            main: path.resolve(__dirname, "src/index.tsx")
        },
        output: {
            publicPath: "/",
            path: path.resolve(__dirname, "build"),
            filename: "[name].bundle.js",
            clean: true,
        },
        devServer: {
            // 포트 번호 설정
            port: 3000,
            // 핫 모듈 교체(HMR) 활성화 설정
            hot: true,
            // gzip 압축 활성화
            compress: true,
            // History 라우팅 대체 사용 설정
            historyApiFallback: true,
            // 개발 서버 자동 실행 설정
            open: true,
            proxy: [
                {
                    context: "/api",
                    target: process.env.API_URL || 'http://localhost:8080',
                    changeOrigin: true,
                }
            ]
        },
        resolve: {
            extensions: [".ts", ".tsx", ".js", ".jsx", "..."],
            alias: {
                "@Components": path.resolve(__dirname, "src/components/"),
                "@Layout": path.resolve(__dirname, "src/components/layout"),
                "@Pages": path.resolve(__dirname, "src/pages/"),
                "@Hooks": path.resolve(__dirname, "src/hooks/"),
                "@Style": path.resolve(__dirname, "src/assets/css"),
                "@Images": path.resolve(__dirname, "src/assets/images"),
                "@Fonts": path.resolve(__dirname, "src/assets/fonts"),
                "@Store": path.resolve(__dirname, "src/store"),
                "@Utils": path.resolve(__dirname, "src/utils"),
                "@Types": path.resolve(__dirname, "src/types"),
            },
        },
        module: {
            rules: [
                {
                    test: /.(ts|js)x?$/,
                    exclude: /node_modules/,
                    use: ["babel-loader", "ts-loader"],
                },
                {
                    test: /\.(woff|woff2|eot|ttf|otf)$/i,
                    type: 'asset/resource',
                    generator: {
                        filename: "[hash][ext]"
                    }
                },
                {
                    test: /\.(png|svg|jpg|jpeg|gif|ico)$/i,
                    type: 'asset/resource',
                    generator: {
                        filename: "[hash][ext]"
                    }
                },
                {
                    test: /\.css$/,
                    use: ["style-loader", "css-loader"],
                },
            ],
        },
        plugins: [
            new webpack.ProvidePlugin({
                process: "process/browser.js",
                React: "react",
            }),
            new HtmlWebpackPlugin({
                template: "./public/index.html",
                favicon: "./public/favicon.png",
                minify:
                    process.env.NODE_ENV === "production"
                        ? {
                            collapseWhitespace: true, // 빈칸 제거
                            removeComments: true, // 주석 제거
                        }
                        : false,
            }),
            new CopyWebpackPlugin({
                patterns: [
                    {from: 'public/robots.txt', to: 'robots.txt'},
                    {from: 'public/manifest.json', to: 'manifest.json'},
                ],
            }),
            // 코드에서 환경 변수를 안전하게 사용할 수 있습니다.
            new webpack.DefinePlugin({
                'process.env.ASSET_PATH': JSON.stringify(ASSET_PATH),
            }),
            new CleanWebpackPlugin(),
        ],
    };
};