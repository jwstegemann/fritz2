const path = require('path');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CssMinimizerPlugin = require("css-minimizer-webpack-plugin");

module.exports = {
    entry: ['./src/scripts/main.js', './src/styles/main.css'],
    output: {
        path: path.resolve(__dirname, '_site/assets'),
        filename: 'main.js'
    },
    plugins: [new MiniCssExtractPlugin()],
    optimization: {
        minimizer: [
            new CssMinimizerPlugin(),
        ],
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                exclude: /node_modules/,
                use: [
                    {loader: MiniCssExtractPlugin.loader},
                    {loader: 'css-loader'},
                    {
                        loader: 'postcss-loader',
                        options: {
                            postcssOptions: {
                                plugins: [
                                    require("tailwindcss"),
                                    require("autoprefixer"),
                                ]
                            }
                        }
                    }
                ]
            }
        ]
    }
};
