package prettypaster;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PrettyPasterTest {

    private final Prettifier prettifierStub = y -> Optional.empty();

    @Test void creation_startsSingleThread_andCanBeStopped() {
        int nrOfThreads = Thread.activeCount();
        try (var x = new PrettyPaster(prettifierStub)) { // Stopping is tested via AutoClosable
            assertEquals(nrOfThreads + 1, Thread.activeCount());
        }
        assertEquals(nrOfThreads, Thread.activeCount());
    }

    @Test void creation_schedulesPollingWith10msFixedDelay() {
        var scheduledPolling = new AtomicBoolean();
        var executorSpy = new ScheduledThreadPoolExecutor(1) {
            @Override public ScheduledFuture<?> scheduleWithFixedDelay(Runnable x, long initialDelay, long delay, TimeUnit unit) {
                scheduledPolling.set(initialDelay == 0 && delay == 10 && unit == TimeUnit.MILLISECONDS);
                return null;
            }
        };

        try (var x = new PrettyPaster(executorSpy, prettifierStub)) {
            assertTrue(scheduledPolling.get());
        }
    }

    @Test void sendsPolledClipboardContent_toPrettifier_andSetsNewContent() throws Exception { // NOTE: edits clipboard
        var expectedIn = UUID.randomUUID().toString();
        var expectedOut = UUID.randomUUID().toString();

        var spy = new Prettifier() {
            String actualIn;
            @Override public Optional<String> prettify(String in) {
                if(actualIn == null) actualIn = in;
                return Optional.of(expectedOut);
            }
        };

        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(expectedIn), null);
        try (var x = new PrettyPaster(spy)) {
            Thread.sleep(2);
        }

        assertEquals(expectedIn, spy.actualIn);
        assertEquals(expectedOut, clipboard.getData(DataFlavor.stringFlavor));
    }

}
