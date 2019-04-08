package prettypaster;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A class that can be used to redirect {@link System#out} and {@link System#err} to nothing.
 * <p/>
 * Usage as a try-with-resources so the redirect will be {@link #close() restored} again.
 * An {@code ignoreOutAndErr(Callable block)} sort of function might have been neater,
 * but it's workability gets less when the {@code block} can throw different checked exceptions.
 */
class OutAndErrIgnorer implements AutoCloseable {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    OutAndErrIgnorer() {
        PrintStream dummyStream = new PrintStream(new OutputStream() { public void write(int b) { /* sink the data */ } });
        System.setOut(dummyStream);
        System.setErr(dummyStream);
    }

    @Override public void close() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
