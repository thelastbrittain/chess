package model;

import java.util.Objects;

public record AuthData(String authToken, String username) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData = (AuthData) o;
        return Objects.equals(username, authData.username) && Objects.equals(authToken, authData.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }
}
