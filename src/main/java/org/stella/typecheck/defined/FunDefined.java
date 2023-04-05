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

        if (t.type == TypesEnum.Fun) {
            ans.append("\t".repeat(k))
                    .append("FUN\n");
            ans.append("\t".repeat(k + 1))
                    .append("{ARGS}\n");

            for (int i = 0; i < t.args.size(); i++)
                ans.append(GetArgsAndReturns(t.args.get(i), k + 1));

            ans.append("\n")
                    .append("\t".repeat(k + 1))
                    .append("{RETURN}\n")
                    .append(GetArgsAndReturns(t.result, k + 1));
        } else {
            ans.append(t.toString(k));
        }

        if (k > 0)
            ans.append("\n");

        return ans.toString();
    }
}
