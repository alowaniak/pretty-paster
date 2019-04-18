package prettypaster;

import prettypaster.base64.Base64Prettifier;
import prettypaster.json.JsonPrettifier;
import prettypaster.xml.XmlPrettifier;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static prettypaster.Prettifier.anyOf;
import static prettypaster.Prettifier.chain;

/**
 * Pretty "pastes" clipboard content using a {@link Prettifier}.
 *
 * Apart from containing the entry-point which wires up the {@link #PrettyPaster(Prettifier)}
 * this class is responsible for polling the clipboard and setting the prettified content.
 *
 * The polling happens in {@link Executors#newSingleThreadScheduledExecutor() it's  own Thread}.
 * The {@link PrettyPaster} is {@link AutoCloseable} but note that {@link #close() closing} waits for task shutdown.
 *
 * NOTE: Some libraries used in polling (and prettifying) write needless output to stdOut/stdErr.
 * To suppress this the {@link System#out} and {@link System#err} are redirected to nothing while polling/prettifying.
 */
public class PrettyPaster implements AutoCloseable {

	private static final PrintStream dummyStream = new PrintStream(new OutputStream() {
		public void write(int b) { /* sink */ }
	});

	public static void main(String[] args) {
		new PrettyPaster(chain(new Base64Prettifier(), anyOf(new XmlPrettifier(), new JsonPrettifier())));
	}

	private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	private final ScheduledExecutorService executor;
	private final Prettifier prettifier;

	public PrettyPaster(Prettifier prettifier) {
		this(Executors.newSingleThreadScheduledExecutor(), prettifier);
	}

	PrettyPaster(ScheduledExecutorService executor, Prettifier prettifier) {
		this.executor = requireNonNull(executor);
		this.prettifier = requireNonNull(prettifier);

		executor.scheduleWithFixedDelay(this::pollClipboardAndPrettify, 0, 100, MILLISECONDS);
	}

	private void pollClipboardAndPrettify() {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
		System.setOut(dummyStream);
        System.setErr(dummyStream);
        try {
            prettifyClipboardContent();
        } finally {
			System.setOut(originalOut);
			System.setErr(originalErr);
		}
	}

	private void prettifyClipboardContent() {
		String clipboardContent;
		try {
			clipboardContent = (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException | IllegalStateException e) {
			return; // Clipboard unavailable or data can't be retrieved (as String), not much we can do
		}
    	prettifier.prettify(clipboardContent).ifPresent(this::setNewContent);
    }

	private void setNewContent(String newContent) {
		try {
			clipboard.setContents(new StringSelection(newContent), null);
		} catch (IllegalStateException e) {
			// Clipboard unavailable, not much we can do
		}
	}

	/**
	 * Stops the polling for the clipboard.
	 *
	 * Closing will {@link ScheduledExecutorService#awaitTermination(long, TimeUnit) wait} max 1 second for termination.
	 *
	 * @throws IllegalStateException if our polling thread was somehow not terminated.
	 */
	@Override public void close() {
		executor.shutdownNow();
		try {
			executor.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		if (!executor.isTerminated()) throw new IllegalStateException("Polling thread not terminated after closing.");
	}
}
