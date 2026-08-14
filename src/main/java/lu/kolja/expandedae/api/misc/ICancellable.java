package lu.kolja.expandedae.api.misc;

/**
 * Helper interface for cancelling all running crafting jobs.
 */
public interface ICancellable {
    /**
     * Cancel all currently running crafting jobs.
     */
    void expandedae$cancelAll();
}
