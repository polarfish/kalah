# Kalah
The game of Kalah in form of web application (built with Spring Boot)

### Game rules

The game provides a Kalah board and a number of seeds or counters. The board has 12 small cups called pits on each side and a big cup called house, at each end. The object of the game is to capture more seeds than one's opponent.

- At the beginning of the game, six seeds are placed in each pit.
- Each player controls the six houses and their seeds on the player's side of the board. The player's score is the number of seeds in the store to their right.
- Players take turns sowing their seeds. On a turn, the player removes all seeds from one of the pits under their control. Moving counter-clockwise, the player drops one seed in each pit in turn, including the player's house but not their opponent's.
- If the last sown seed lands in an empty pit owned by the player, and the opposite pit contains seeds, both the last seed and the opposite seeds are captured and placed into the player's house.
- If the last sown seed lands in the player's house, the player gets an additional move. There is no limit on the number of moves a player can make in their turn.
- When one player no longer has any seeds in any of their pits, the game ends. The other player moves all remaining seeds to their house, and the player with the most seeds in their house wins.

### Running the game

$ ./gradlew bootRun

The game is configured to work on port 80

### Screenshots

![Screenshot of Kalah](/src/main/resources/static/screenshot.png?raw=true)
