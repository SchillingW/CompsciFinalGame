public class MultiKeyMap<T, U> {

    private final T[][] keys;
    private final U[] values;

    public MultiKeyMap(T[][] keys, U[] values) {
        this.keys = keys;
        this.values = values;
    }

    public U get(T key) {
        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < keys[i].length; j++) {
                if (key.equals(keys[i][j])) return values[i];
            }
        }
        return null;
    }
}
