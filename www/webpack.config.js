const path = require('path');

module.exports = {
    entry: ['./src/scripts/main.js', './src/styles/main.css'],
    output: {
        path: path.resolve(__dirname, '_site/assets'),
        filename: 'main.js'
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                exclude: /node_modules/,
                use: [
                    {loader: 'style-loader'},
                    {loader: 'css-loader'},
                    {
                        loader: 'postcss-loader',
                        options: {
                            postcssOptions: {
                                plugins: [
                                    require("tailwindcss"),
                                    require("autoprefixer"),
                                    require("cssnano")
                                ]
                            }
                        }
                    }
                ]
            }
        ]
    }
};
