package site.halenspace.pocket.threadpool.config.property.chain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
@Slf4j
public abstract class ChainLink<T> {

    private final AtomicReference<ChainLink<T>> aReference;

    /** The next node in the chain.  */
    private final ChainLink<T> next;

    /** Callback methods for all nodes, corresponding to the order of the chain.  */
    private final List<Runnable> callbacks;

    public abstract String getName();

    protected abstract T getValue();

    public abstract boolean isValueAcceptable();

    /**
     * No arg constructor - used for end node.
     * node -> {@link ChainLink}
     */
    public ChainLink() {
        this.next = null;
        this.aReference = new AtomicReference<>(this);
        this.callbacks = new ArrayList<>();
    }

    /**
     * @param next The next node in the chain.
     *             node -> {@link ChainLink}
     */
    public ChainLink(ChainLink<T> next) {
        this.next = next;
        this.aReference = new AtomicReference<>(next);
        this.callbacks = new ArrayList<>();
    }

    protected void checkAndFlip() {
        // in case this is the end node
        if (next == null) {
            aReference.set(this);
            return;
        }

        if (this.isValueAcceptable()) {
            log.debug("Flipping property: {} to use its current value: {}", getName(), getValue());
            aReference.set(this);
        } else {
            log.debug("Flipping property: {} to use NEXT property: {}", getName(), next);
            aReference.set(next);
        }

        for (Runnable r : callbacks) {
            r.run();
        }
    }

    //    public T get() {
//        if (aReference.get() == this) {
//            return this.getValue();
//        }
//        else {
//            return aReference.get().get();
//        }
//    }
    public T get() {
        T value;
        if ((value = this.getValue()) != null) {
            return value;
        }

        if (next != null) {
            return next.get();
        }

        return null;
    }

    /**
     * @param r callback to execute
     */
    public void addCallback(Runnable r) {
        callbacks.add(r);
    }
}
