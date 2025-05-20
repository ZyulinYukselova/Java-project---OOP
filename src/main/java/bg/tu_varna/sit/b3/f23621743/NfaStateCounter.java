package bg.tu_varna.sit.b3.f23621743;

public class NfaStateCounter {
    private static int counter = 0;

    public static int next() {
        return counter++;
    }

    public static void reset() {
        counter = 0;
    }
}
