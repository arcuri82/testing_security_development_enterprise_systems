const path = require('path');

module.exports = {
    entry: './src/main/js/index.jsx',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'target', 'classes', 'static'),
    },
    module: {
        rules: [
            {
                test: /\.jsx$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                }
            }
        ]
    },
    resolve: {
        extensions: ['.js', '.jsx']
    }
};