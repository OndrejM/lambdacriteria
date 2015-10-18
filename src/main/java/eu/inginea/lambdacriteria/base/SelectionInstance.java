package eu.inginea.lambdacriteria.base;

import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.alternative1.Selector;
import javax.persistence.criteria.Root;

public class SelectionInstance {
    private final Alias<?> alias;
    private Root<?> instance;
    private Integer parameterIndex;
    private final Selector<?> selector;

    public SelectionInstance(Alias<?> alias) {
        this.alias = alias;
        this.selector = null;
    }

    public SelectionInstance(Alias<?> alias, Root<?> instance) {
        this.alias = alias;
        this.instance = instance;
        this.selector = null;
    }

    public SelectionInstance(Selector<?> selector) {
        this.selector = selector;
        this.alias = null;
    }

    public Alias<?> getAlias() {
        return alias;
    }

    public Root<?> getInstance() {
        return instance;
    }

    public void setInstance(Root<?> instance) {
        this.instance = instance;
    }

    public Selector<?> getSelector() {
        return selector;
    }

    public Integer getParameterIndex() {
        return parameterIndex;
    }

    public void setParameterIndex(Integer parameterIndex) {
        this.parameterIndex = parameterIndex;
    }

}
