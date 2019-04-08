package prettypaster;

import prettypaster.base64.Base64Prettifier;
import prettypaster.xml.XmlPrettifier;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Pretty "pastes" clipboard content using a {@link Prettifier}.
 *
 * Apart from containing the entry-point which wires up the {@link #PrettyPaster(Prettifier)}
 * this class is responsible for polling the clipboard and setting the prettified content.
 *
 * The polling happens in {@link Executors#newSingleThreadScheduledExecutor() it's  own Thread}.
 * The {@link PrettyPaster} is {@link AutoCloseable} but note that {@link #close() closing} waits for task shutdown.
 */
public class PrettyPaster implements AutoCloseable {

	public static void main(String[] args) {
		new PrettyPaster(Prettifier.chainable(new Base64Prettifier(), new XmlPrettifier()));
	}

	private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	private final ScheduledExecutorService executor;
	private final Prettifier prettifier;

	public PrettyPaster(Prettifier prettifier) {
		this(Executors.newSingleThreadScheduledExecutor(), prettifier);
	}

	PrettyPaster(ScheduledExecutorService executor, Prettifier prettifier) {
		this.executor = executor;
		this.prettifier = prettifier;

		executor.scheduleWithFixedDelay(this::pollClipboardAndPrettify, 0, 10, MILLISECONDS);
	}

	private void pollClipboardAndPrettify() {
		try (OutAndErrIgnorer ignoreNeedlessPrintsByLibraryCode = new OutAndErrIgnorer()) {
			String content = (String) clipboard.getData(DataFlavor.stringFlavor);
			prettifier.prettify(content).ifPresent(this::setNewContent);
		} catch (UnsupportedFlavorException | IOException | IllegalStateException e) {
			// Clipboard unavailable or data can't be retrieved (as String), not much we can do
		}
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
