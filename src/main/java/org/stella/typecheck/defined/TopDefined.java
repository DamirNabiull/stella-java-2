package org.stella.typecheck.defined;

public class TopDefined extends DefinedType {
    public TopDefined() {
        super(TypesEnum.Top);
    }

    @Override
    public boolean equals(DefinedType o) {
        return true;
    }

    @Override
    public boolean equals(DefinedType o, String context) {
        return true;
    }
}
