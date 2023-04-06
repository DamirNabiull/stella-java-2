package org.stella.typecheck;

import org.stella.typecheck.defined.DefinedType;
import org.stella.utils.ExceptionsUtils;

import java.util.HashMap;

public class Context {
    public HashMap<String, DefinedType> GlobalDefinitions;

    public HashMap<String, DefinedType> LocalDefinitions;

    public DefinedType MatchType;

    public Context()
    {
        GlobalDefinitions = new HashMap<>();
        LocalDefinitions = new HashMap<>();
        MatchType = null;
    }

    public Context(Context context)
    {
        GlobalDefinitions = new HashMap<>(context.GlobalDefinitions);
        GlobalDefinitions.putAll(context.LocalDefinitions);

        LocalDefinitions = new HashMap<>();
        MatchType = context.MatchType;
    }

    public void addLocal(String key, DefinedType value) {
        LocalDefinitions.put(key, value);
    }

    public void clearLocal(){
        LocalDefinitions.clear();
    }

    public void addGlobal(String key, DefinedType value) {
        GlobalDefinitions.put(key, value);
    }

    public DefinedType lookUp(String name) {
        if (LocalDefinitions.containsKey(name))
            return LocalDefinitions.get(name);

        if (GlobalDefinitions.containsKey(name))
            return GlobalDefinitions.get(name);

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