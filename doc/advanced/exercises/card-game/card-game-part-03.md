# Card Game - Part 03

In the REST API of `user-collections`, add the following endpoint:

`PATCH /api/user-collections/{userId}`

This is going to be used for doing the following actions:

* sell a card
* mill a card
* open a card pack

We are going to use a custom `PATCH` semantics, where
we are going to pass a JSON object with 2 fields:
a "command" (either OPEN_PACK, MILL_CARD or BUY_CARD) and
an optional card id. 
This will be represented in `dto/PatchUserDto`.
When opening a pack, the response of the `PATCH` should
contain the ids of all the cards in the pack (`dto/PatchResultDto`).

Obviously, one should not be able to buy a card if not enough money,
or open a pack if s/he does not have any left.

In `RestAPITest`, add one RestAssured test of each of these 3 commands.
Furthermore, you will need to configure as well a `FakeCardService`.
Add enough tests (of your choice, not just in `RestAPITest`) 
to get at least 70% coverage on the whole module.


Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-03` module.

Note: in some cases, to avoid copy&paste of code, we are going to re-use
modules from the previous solutions, if their code has not changed.
This is for example the case here for `cards-dto`. 
