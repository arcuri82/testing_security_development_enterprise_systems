const express = require('express');
const path = require('path');


//const proxy = require('express-http-proxy');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();


if(process.env.DEV){
    const proxiedServer = 'http://128.0.0.1:80'
    app.use("/api/*", createProxyMiddleware({ target: proxiedServer, changeOrigin: true }))
}

//needed to server static files, like HTML, CSS and JS.
app.use(express.static('public'));

//handling 404
app.use((req, res, next) => {
    res.sendFile(path.resolve(__dirname, '..', '..', 'public', 'index.html'));
});

module.exports = {app};
