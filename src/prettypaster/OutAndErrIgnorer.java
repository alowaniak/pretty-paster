package prettypaster;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A class that can be used to redirect {@link System#out} and {@link System#err} to nothing.
 * Usage as a try-with-resources
 */
class OutAndErrIgnorer implements AutoCloseable {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    OutAndErrIgnorer() {
        var dummyStream = new PrintStream(new OutputStream() { public void write(int b) { /* sink the data */ } });
        System.setOut(dummyStream);
        System.setErr(dummyStream);
    }

    @Override public void close() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
