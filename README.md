# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

My Diagram: 
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMLxfH8AJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfAMUCDBYlgOLCoAKDdLh6UjyJDd4AXOYECkHUoEAQpA0FheDENRdFYmxQcCiTFlShiAA1SgkDFVAODNOFGJgG0lkLJlkwKd1uT5AVqytNTg1tcVJWrCdRAAHmnCTSR0CAIG4dhLJUcTe2THsi20-JjFclB3O8yS0wzBtcxrUZNJdVQdI5b1fUFP8L3bGMR38mzcjslMkvHOioAyziBN4lc11vfLpzy0oGIPQNOwbc90AOB8MAK4pLhfJwYBImqyLq7MGrQA4QLA7w-ECLwUDbATfGYZD0kyTBmuYJUd3nCppF5Ll6i5ZoWgI1QiO6HK0DK2i2Po47WKBSgOOVIduPsGb+IQmahIxUS7sCrTJJkuSFJQDhM2zWFjqi4tYtKAAxcIagAWT61IYAAcS2hGBvS2yPMkhynPTE6xPyLLSg4FBuEyIGc1BrzvvUCHobhhGZE27a0f-DHMqx0k1Q1Uw8ta7Uh2mrMSoQLBqIyyruc1Jb+ZKUoOrMTgRogwJIQ4ODoWR-NWTm1DFvQvIBzataUbwlp7HzI76rZ6izuusoekuzA8tugWuOhJHtf4j3tbekT-K+6KpNUWSoHkxSKZB62LzBnz3Xp+GR2R1HjvZomceck6A8JzmU2QWJPdGVRYVjlk6Zh+GLdFU2YAUHkeRgKuUA9aRZhQzJlKMnQEFAZtO9rRv8y9fM09z0om5b1Q2-m00eqnmBu97-ui9mJvh6L53zvK3JOPzsBC7UEWxeNiWt6qqYm9UcZKn6CfpGvgBGJ8AGYABYnnb2fP3uPovkXkA+49RWF8Ne+YJh9D2DARoTUDZgFlu1V874L7a2vhUW++YW6Pxfu-KYn9l4-z-j3AB+DgFPFAaMcBkDoGK1AuBMaARsA+CgNgbg8BjSZC1qKXWC0lqYWNuUHCDRdpNytv1f875yEoCosbO2c4qrHVXkPfMV05yu0nCqEKmQD6wjgOwlAB8-ZYmzkTX6Yd-qAzClTLK5cGZJxrqnQcGV06OUzsYsemj9H5lhJI2YaAUDJAPi3UutNfJxQrFWHxMA-EBIwdIFKg9RijyCqSO+m97ay3BLo9MWj8xHzKqfe2VU74wJyHA6c1F5avi6r0QJ0ghpK08KNSClhSbcWSDAAAUhAXinDDABH-s2fWpS+GrQEVUSkZsRHBGjugd8LDgAtKgHAJy0BFGjBbtI1asjgTyJmWga0PdFnLO4lANZzc6lpNUfkQcbjkkaBdtcu6pQABW3S0DaK6bxAxKA0TvVuTTFMpjw4A0jlY3ONjE4NmToKSAF4kkAtKBnPG-yg4lHkpYbRd9gkxVCVDCuCTDA1ybvCoOxKCZEz8FoHJoxwr0mptFCG8VKz+h6vEpuMASXFlKFLS5wIMkqk+e83JahSriwqmfHlMtyn8IVsNRpKsAheAWfAZysBgDYBYYQeIiQUgzyGRhFactKgbT5NtXaflbb5EluqTUDzOIgFVQoDVyAQDar4ogdMUAnWatdQQRIhiPoC0Dlyh1nrI7YnpeDHOdzI1xw5CaraXJa71wRiikNqqD7F2xRDBNLM64NzJZ9aNCLQ14GUhoaxuLc1JvzRZPm0q3YerwN6l1bq8lityNanmUqd4ytfDQoAA
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
