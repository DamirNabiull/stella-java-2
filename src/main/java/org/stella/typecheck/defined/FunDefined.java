package org.stella.typecheck.defined;

public class FunDefined extends DefinedType {
    public FunDefined() {
        super(TypesEnum.Fun);
    }

    public FunDefined(DefinedType a, DefinedType r) {
        super(TypesEnum.Fun, a, r);
    }

    @Override
    public String toString() {
        return GetArgsAndReturns(this, 0);
    }

    @Override
    public String toString(int k) {
        return GetArgsAndReturns(this, k);
    }

    private String GetArgsAndReturns(DefinedType t, int k){
        String ans = "";

        if (t.type == TypesEnum.Fun) {
            ans += "\t".repeat(k) + "FUN\n";
            ans += "\t".repeat(k + 1) + "{ARGS}\n" + GetArgsAndReturns(t.args, k+1);
            ans += "\n" + "\t".repeat(k + 1) + "{RETURN}\n" + GetArgsAndReturns(t.result, k+1);
        } else {
            ans += t.toString(k);
        }

        if (k > 0)
            ans += "\n";

        return ans;
    }
}
