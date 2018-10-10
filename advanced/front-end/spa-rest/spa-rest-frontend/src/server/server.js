const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const app = express();


/*
    Needed to server static files, like HTML, CSS and JS.
    So, anything under the "public" folder can be served directly
    from the server at the base url, eg
    http://localhost:8080/foo/bar.html
    will search for "foo/bar.html" under "public" and return it
    if present.
 */
app.use(express.static('public'));


/*
    This is a bit tricky. Because we are dealing with a SPA, we should never
    have a 404 (Not Found) directly on the server.
    The __LAST__ "app.use" you call handles the case of a resource not found, which
    by default would give a 404.
    Here, we override such behavior to return the HTML content of index.html.
    Why?
    This is to handle SPA-Routing, eg issue with bookmarks and refreshing a page.
    As we return index.html, it will be able to re-render itself correctly based
    on the URL address bar in the browser (ie, using React-Router).
    If a resource does not exist, then that will be handled in JS in the home-page,
    eg see the component "not_found.jsx".

    Note: this is very different from returning a 3xx redirecting to the home-page,
    because that would change the address bar, and so what would be displayed.

    Note: this is not enough for SEO (Search-Engine Optimization), as most crawlers
    do not handle JS. We would need to do server-side rendering instead of returning
    the content of "index.html". But as it is quite tricky, we will not see it in this
    course.
 */
app.use((req, res, next) => {
    res.sendFile(path.resolve(__dirname, '..', '..', 'public', 'index.html'));
});

const port = 8080;

app.listen(port, () => {
    console.log('Starting Server on port ' + port);
});

