# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

My Diagram: 
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMLxfH8AJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfAMUCDBYlgOLCoAKDdLh6UjyJDd4AXOYECkHUoEAQpA0FheDENRdFYmxQcCiTFlShKJAxUsM04UYmAbSWQsmWTAp3W5PkBWrK0lODW1xUlasJ1EAAeacJNJRTlPeUyVHE3tkx7It1PyYx7JQRzXMktMMwbXMa1GVSXVUDSOW9X1BT-C92xjEdPIs3IrJTGLxzoqAks4gTeJXNdb0y6cMtKBiD0DTsG3PdADgfDAsuKS4XycGASLKsiKuzKq0AOECwO8PxAi8FA2wE3xmGQ9JMkwWrmCVHd5wqaReS5eouWaFoCNUIjujStACtotj6N21igUoDjlSHbj7DG-iELGoSMVEi7vLUyTpNkzNs1hXaQuLcLSgAMXCGoAFkOtSGAAHEVvBrrEsspzJN2zyXtC0oOBQbhMk+nMfpc171H+oHQfBmRltW2H-3h5LEdJNUNVMDL6u1IdRqzPKECwaikuK+nNRm5mSlKJqzE4PqIMCSEODg6EofzVkJtQ6b0LyAcGoW6G8Jaex8x2yqqeog7TrKHpjswDLzpZrjoUh+X+Jt+WHpElH8hSqTKBkywce+-WL1+tz3WJsGRyhmHkcHJK3eRl23fJMAFB5HlbdGVRYX9lkieBsGddFTWYATnkYBzlAPWkWYUMyeS9J0BBQGbKvayL-MvXzanY-zUvVHLybTTaruYBruuG5T2Zi5blPzcOwrck45BYmTtQOa59WeankqpmL1Rxkqfpi9L7eAEYnwAZgAFieCve8-e4+i+QeQHrtqVi+Mf8wmPo9hgRoapVsBBca1874N7y23hUXeHdpCHxPufKYl9h43zvrXB+8Dn5PFfqMd+n9v6i1AuBAaARsA+CgNgbg8BjSZDlqKRWU0ZqYXVuUHCDR1rFz1p1f8750EoCourI2c4Sq7VHs3fMJ05yW0nCqPymQF6wjgOQlAC8nZYhjrTFM70vYBTxilTOJMQ553DhdSOKjSj6JZqjYspRJHyPzLCThsw0AoGSAvUu6dCbuQihWKstiYD2McRAuKTdRhtyMXvaQk9jaC3BLI9MUj8xLwKqvY2JUQk-xyH-ac1Fhavhar0Jx0gepi08P1SClhMbcWSDAAAUhAXilDDABHvs2ZWqS6HzQYVUSkWsWHBF9ugd8JDgAlKgHACA3EoCCNGKXbh81eHAn4T0tA1pa6DOGaM8ZJc8lhNEfkQcyifLqE2exbZF1SgACtqloGkVU3iCiUBokersgmqiPYfQ0fMlxYU3GAyzqTPOkALxBL2aleZDy0ZqOkSE952js75lDoKYuALHmlHhWJV2RiznXOsaRSFnzIqVn9G1fxxcYAIrRnzA5lAIkqiuRc2Jah8rcyKmvMlAt0n0JFr1QpEsAheAGfAbgeBOzYBIYQeIiQUg9yaRhOaQtKhLT5KtdaHlDb5F5uqTUFsjlWxAPyqACghXIBAKKviiB0y6v1SKggiRFFPVMaiwFFidXe2xPjUKZjnJaM+XKlaXJ86J3BiC8x2rTUL1Tti90XqKYFwCWIFFbsg14HkhoD14byY+qjTccl09OImrwHq4VhrLVoDiQy3IqqGYspnmy18OCgA
## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
