// mapping of values to multiple key objects
public class MultiKeyMap<T, U> {

    // key and value mappings
    private final T[][] keys;
    private final U[] values;

    // initialize map
    public MultiKeyMap(T[][] keys, U[] values) {
        this.keys = keys;
        this.values = values;
    }

    // get value from one key
    public U get(T key) {

        // iterate through key sets
        for (int i = 0; i < keys.length; i++) {

            // iterate through possible keys in set
            for (int j = 0; j < keys[i].length; j++) {

                // if keys match then return value
                if (key.equals(keys[i][j])) return values[i];
            }
        }

        // return no object found
        return null;
    }
}
