# Card Game - Part 05

We are going to implement a third new REST API,
in a new module called `cards`.
This service will provide the statics info on all the cards
in the games, re-using the DTOs from `cards-dto`.

This new API should have the same structures and features of `user-collections`
and `scores`, e.g.:

* A self-executable uber-jar file.
* Wrapped responses with custom error handling.
* Documentation with SpringFox.
* Test cases (e.g., with RestAssured) achieving at least 70% code coverage in total.

However, as the data is static, there is no need to handle a database.

The API needs to implement the following endpoints:

* `GET /api/cards/collection_v1_000`, returning the `CollectionDto` with the data of
  all the cards in the game. Should instruct all caches that this data can be
  cached for 1 year (when there is a new version, anyway the URL will change).  
* `GET /api/cards/collection_v0_001`, representing/simulating an old version of the
    cards. This should do now a permanent redirection to the most recent version.
* `GET /api/cards/imgs/{imgId}`, get an actual image for the given id. This is the same
    id used in `CardDto`. 
    For real images, you can use free ones from for example 
    Flaticon, like [these ones](https://www.flaticon.com/packs/monsters-8).
    You can store the images under `src/main/resources`.
    If you choose the SVG format, the endpoint can just return a `String` with 
    data-type `image/svg+xml` (no wrapped response).   
    You can use `javaClass.getResource(path)?.readText()` to read the text
    in a resource (note that SVG images are just XML files...). 
    Should instruct all caches that these images can be cached for 1 year.
    Note: an easy way to check if this endpoint is working correctly, is to open
    it in the browser, and see if the image is correctly displayed.  
    
Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-05` module.   