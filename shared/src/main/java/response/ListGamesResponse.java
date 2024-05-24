package response;

import model.GameData;

import java.util.Collection;

public record ListGamesResponse(Collection<GameData> games, String message) {
}
