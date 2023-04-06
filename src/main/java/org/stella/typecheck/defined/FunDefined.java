package org.stella.typecheck.defined;

public class FunDefined extends DefinedType {
    public FunDefined() {
        super(TypesEnum.Fun);
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
        StringBuilder ans = new StringBuilder();

        ans.append("\t".repeat(k))
                .append("FUN\n");
        ans.append("\t".repeat(k + 1))
                .append("{ARGS}\n");

        for (int i = 0; i < t.args.size(); i++)
            ans.append(t.args.get(i).toString(k+1));

        ans.append("\n")
                .append("\t".repeat(k + 1))
                .append("{RETURN}\n")
                .append(t.result.toString(k+1));

        if (k > 0)
            ans.append("\n");

        return ans.toString();
    }
}
