package org.stella.typecheck;

import org.stella.typecheck.defined.DefinedType;
import org.stella.utils.ExceptionsUtils;

import java.util.HashMap;

public class Context {
    public HashMap<String, DefinedType> GlobalDefinitions;

    public HashMap<String, DefinedType> LocalDefinitions;

    public Context()
    {
        GlobalDefinitions = new HashMap<>();
        LocalDefinitions = new HashMap<>();
    }

    public Context(Context context)
    {
        GlobalDefinitions = new HashMap<>(context.GlobalDefinitions);
        GlobalDefinitions.putAll(context.LocalDefinitions);

        LocalDefinitions = new HashMap<>();
    }

    public DefinedType LookUp(String name) {
        if (GlobalDefinitions.containsKey(name))
            return GlobalDefinitions.get(name);

        if (LocalDefinitions.containsKey(name))
            return LocalDefinitions.get(name);

        ExceptionsUtils.throwUndefinedException(name);
        return null;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("Global:\n");

        for (String key : GlobalDefinitions.keySet()) {
            ans.append(key).append(" : ").append(GlobalDefinitions.get(key));
        }

        ans.append("\nLocal:\n");

        for (String key : LocalDefinitions.keySet()) {
            ans.append(key).append(" : ").append(LocalDefinitions.get(key));
        }

        return ans.toString();
    }
}