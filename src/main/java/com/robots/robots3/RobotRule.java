package com.robots.robots3;

import java.util.ArrayList;
import java.util.List;

public class RobotRule
{
    private String userAgent;
    private List<String> ruleList ;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public List<String> getRuleList() {
        return ruleList;
    }

    public void setRule(String rule) {
        this.ruleList.add(rule);
    }

    RobotRule() {
        ruleList = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" " + this.userAgent + NEW_LINE);
        for (String rule : ruleList) {
            result.append(" " + rule + NEW_LINE);
        }
        result.append("}");
        return result.toString();
    }
}
