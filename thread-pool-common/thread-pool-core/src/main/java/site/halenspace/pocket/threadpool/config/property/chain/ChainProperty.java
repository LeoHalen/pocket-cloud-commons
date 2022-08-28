package site.halenspace.pocket.threadpool.config.property.chain;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocket.threadpool.config.property.DynamicProperty;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
@Slf4j
public class ChainProperty<T> extends ChainLink<T> {

    private final DynamicProperty<T> dProp;

    public ChainProperty(DynamicProperty<T> dProp) {
        super();
        this.dProp = dProp;
    }

    public ChainProperty(DynamicProperty<T> dProp, ChainProperty<T> next) {
        super(next); // setup next pointer
        this.dProp = dProp;
        dProp.addCallback(() -> {
            log.debug("Property changed: '{} = {}'", getName(), getValue());
            super.checkAndFlip();
        });
        super.checkAndFlip();
    }

    @Override
    public String getName() {
        return dProp.getPropName();
    }

    @Override
    protected T getValue() {
        return dProp.get();
    }

    @Override
    public boolean isValueAcceptable() {
        return dProp.get() != null;
    }
}
