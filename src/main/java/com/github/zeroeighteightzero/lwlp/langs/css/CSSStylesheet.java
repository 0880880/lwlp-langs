package com.github.zeroeighteightzero.lwlp.langs.css;

import java.util.HashMap;
import java.util.Map;

public class CSSStylesheet {

    public final Map<CSSSelector, CSSRule> rules = new HashMap<>();
    public final Map<CSSSelector, CSSScope> scopes = new HashMap<>();

    public Map<CSSSelector, CSSRule> getRules() {
        return rules;
    }

    public CSSRule getRule(CSSSelector selector) {
        return rules.get(selector);
    }

    public CSSScope getScope(CSSSelector scope) {
        if (!scopes.containsKey(scope))
            scopes.put(scope, new CSSScope());
        return scopes.get(scope);
    }

}
