package dev.kmunton.api.core.joke;

import java.util.Objects;

public class JokeDTO {
    private String id;
    private String joke;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Joke{" +
                "id='" + id + '\'' +
                ", joke='" + joke + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokeDTO jokeDTO = (JokeDTO) o;
        return Objects.equals(id, jokeDTO.id) &&
                Objects.equals(joke, jokeDTO.joke) &&
                Objects.equals(status, jokeDTO.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, joke, status);
    }
}