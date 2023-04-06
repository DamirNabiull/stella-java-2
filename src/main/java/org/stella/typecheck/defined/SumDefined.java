package org.stella.typecheck.defined;

public class SumDefined extends DefinedType {
    public SumDefined() {
        super(TypesEnum.Sum);
    }

    @Override
    public String toString() {
        return GetLabels(this, 0);
    }

    @Override
    public String toString(int k) {
        return GetLabels(this, k);
    }

    @Override
    public String toString(String name, int k) {
        return GetLabels(this, name, k);
    }

    private String GetLabels(DefinedType t, int k){
        StringBuilder ans = new StringBuilder();

        ans.append("\t".repeat(k))
                .append("SUM\n");

        for (var key : labels.keySet())
            ans.append("\t".repeat(k + 1)).append(labels.get(key).toString(key,k+1));

        return ans.toString();
    }

    private String GetLabels(DefinedType t, String name, int k){
        StringBuilder ans = new StringBuilder();

        ans.append("\t".repeat(k))
                .append(name)
                .append(" : ")
                .append("SUM\n");

        for (var key : labels.keySet())
            ans.append("\t".repeat(k + 1)).append(labels.get(key).toString(key,k+1));

        return ans.toString();
    }
}