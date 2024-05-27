public final class Result<S, F> {
    
    private S s;
    
    private F f;

    public static <S, F> Result<S, F> success(S s) {
        Result<S, F> result = new Result<S, F>(s, null);
        return result;
    }

    public static <S, F> Result<S, F> fail(F f) {
        Result<S, F> result = new Result<S, F>(null, f);
        return result;
    }

    private Result(S s, F f) {
        this.s = s;
        this.f = f;
    }

    public boolean isSuccess() {
        return s != null;
    }

    public boolean isFail() {
        return f != null;
    }

    public S getSuccess() {
        return s;
    }

    public F getFail() {
        return f;
    }

    public static final class Dummy {}
}
