package com.neo.drools.model;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

/**
 * Created by zhuzs on 2017/7/19.
 */
public class MyAgendaFilter implements AgendaFilter{

    private String ruleName;

    public MyAgendaFilter(String ruleName) {
        this.ruleName = ruleName;
    }

    @Override
    public boolean accept(Match match) {
        System.out.println("match.getRule().getName(): "+ match.getRule().getName());
        System.out.println("ruleName: "+ ruleName);
        return match.getRule().getName().equals(ruleName) ? true : false;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
