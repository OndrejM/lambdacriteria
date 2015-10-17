package eu.inginea.lambdacriteria.base;

import eu.inginea.lambdacriteria.Alias;
import javax.persistence.criteria.Root;

public class AliasInstance {
    private final Alias<?> alias;
    private Root<?> instance;
    private int parameterIndex;

    public AliasInstance(Alias<?> alias) {
        this.alias = alias;
    }

    public AliasInstance(Alias<?> alias, Root<?> instance) {
        this.alias = alias;
        this.instance = instance;
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

    public int getParameterIndex() {
        return parameterIndex;
    }

    public void setParameterIndex(int parameterIndex) {
        this.parameterIndex = parameterIndex;
    }

}
