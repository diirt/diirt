package gov.bnl.pvmanager;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * A source for data that is going to be processed by the PVManager.
 * PVManager can work with more than one source at a time. Support
 * for each different source can be added by external libraries.
 *
 * @author carcassi
 */
public abstract class DataSource {

    private static Logger logger = Logger.getLogger(DataSource.class.getName());

    /**
     * Helper class that contains the logic for processing a new value.
     * It takes care of locking the collector and calling the disconnect
     * when appropriate.
     *
     * @param <T>
     */
    protected static abstract class ValueProcessor<T, E> {

        private final WeakReference<Collector> collectorRef;
        private final ValueCache<E> cache;

        public ValueProcessor(Collector collector, ValueCache<E> cache) {
            collectorRef = new WeakReference<Collector>(collector);
            this.cache = cache;
        }

        public void processValue(T payload) {
            // Get the collector. If it was garbage collected,
            // remove the monito
            Collector c = collectorRef.get();
            if (c == null) {
                logger.fine("Removing monitor " + this);
                close();
            }

            // Lock the collector and prepare the new value.
            synchronized (c) {
                try {
                    if (updateCache(payload, cache))
                        c.collect();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Value processing failed", e);
                }
            }
        }

        /**
         * Called by the framework if this callback is no longer needed.
         */
        public abstract void close();

        /**
         * Implements the update of the cache given the protocol specific payload.
         *
         * @param payload the payload of the notification
         * @param cache the cache to update
         * @return true if an update is needed; false if not
         */
        public abstract boolean updateCache(T payload, ValueCache<E> cache);

    }

    public abstract void monitor(MonitorRecipe connRecipe);
}
