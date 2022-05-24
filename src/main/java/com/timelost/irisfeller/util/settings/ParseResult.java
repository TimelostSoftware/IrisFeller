package com.timelost.irisfeller.util.settings;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class ParseResult<R> {

    private final R result;
    private final List<String> error;

    public static <R> ParseResult<R> of(R result) {
        return new ParseResult<>(result, null);
    }

    public static <R> ParseResult<R> error(String... error) {
        return new ParseResult<>(null, Arrays.asList(error));
    }

    private ParseResult(R result, List<String> error) {
        this.result = result;
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }

    public boolean hasResult() {
        return result != null;
    }

    public void withResult(Consumer<R> consumer) {
        consumer.accept(this.result);
    }

    public void withError(Consumer<List<String>> consumer) {
        consumer.accept(error);
    }
}
