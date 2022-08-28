package site.halenspace.pocket.threadpool.config.property.chain;

import site.halenspace.pocket.threadpool.config.property.DynamicProperty;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public class ChainDynamicProperty<T> implements DynamicProperty<T> {

    private final ChainProperty<T> chainProperty;

    public ChainDynamicProperty(ChainProperty<T> chainProperty) {
        super();
        this.chainProperty = chainProperty;
    }

    @Override
    public String getPropName() {
        return chainProperty.getName();
    }

    @Override
    public void addCallback(Runnable callback) {
        chainProperty.addCallback(callback);
    }

    @Override
    public T get() {
        return chainProperty.get();
    }
}