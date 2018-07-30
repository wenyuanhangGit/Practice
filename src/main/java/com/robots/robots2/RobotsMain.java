package com.robots.robots2;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RobotsMain {

    private static final String DISALLOW = "Disallow";

    public static void main(String[] args){
        String taoBaoUrl = "https://www.taobao.com/oshtml";
        String weiboUrl = "https://www.weibo.com/robots.txt";
        String renrenUrl = "http://www.people.com.cn/robots.txt";
        String zhihuUrl = "https://www.zhihu.com/login/";
        String baiduUrl = "https://www.baidu.com/link";
        String googleUrl = "https://www.google.com/robots.txt";

        String userAgent = "Googlebot";
        System.out.println(robotSafe(taoBaoUrl, userAgent));
    }

    private static boolean robotSafe(String strUrl, String userAgent)
    {
        URL url;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            return false;
        }

        userAgent = userAgent.trim();
        String strRobot = url.getProtocol() + "://" + url.getHost() + "/robots.txt";
        URL urlRobot;
        try { urlRobot = new URL(strRobot);
        } catch (MalformedURLException e) {
            // something weird is happening, so don't trust it
            return false;
        }

        String strCommands;
        try
        {
            InputStream urlRobotStream = urlRobot.openStream();
            byte b[] = new byte[1024];
            int numRead = urlRobotStream.read(b);
            StringBuilder sb = new StringBuilder();
            sb.append(new String(b, 0, numRead));
            while (numRead != -1) {
                numRead = urlRobotStream.read(b);
                if (numRead != -1)
                {
                    String newCommands = new String(b, 0, numRead);
                    sb.append(newCommands);
                }
            }
            strCommands = sb.toString();
            urlRobotStream.close();
        }
        catch (IOException e)
        {
            return true; // if there is no robots.txt file, it is OK to search
        }

        if (strCommands.contains(DISALLOW)) // if there are no "disallow" values, then they are not blocking anything.
        {
            String[] split = strCommands.split("\n");
            ArrayList<RobotRule> robotRules = new ArrayList<>();
            String mostRecentUserAgent = null;
            String oldUserAgent = null;
            RobotRule r = new RobotRule();
            for (String aSplit : split) {
                String line = aSplit.trim();
                if (line.toLowerCase().startsWith("user-agent")) {
                    int start = line.indexOf(":") + 1;
                    int end = line.length();
                    mostRecentUserAgent = line.substring(start, end).trim();
                    if(!mostRecentUserAgent.equals(oldUserAgent)){
                        r = new RobotRule();
                        oldUserAgent = mostRecentUserAgent;
                        r.setUserAgent(mostRecentUserAgent);
                        robotRules.add(r);
                    }
                } else if (line.startsWith(DISALLOW)) {
                    if (mostRecentUserAgent != null) {
                        int start = line.indexOf(":") + 1;
                        int end = line.length();
                        r.setRule(line.substring(start, end).trim());
                    }
                }
            }

            RobotRule userAgentRobotRule = null;
            for (RobotRule robotRule : robotRules){
                if(robotRule.getUserAgent().contains(userAgent)){
                    userAgentRobotRule = robotRule;
                    break;
                }
            }

            if(userAgentRobotRule == null){
                for (RobotRule robotRule : robotRules){
                    if(robotRule.getUserAgent().contains("*")){
                        userAgentRobotRule = robotRule;
                        break;
                    }
                }
            }

            System.out.println(userAgentRobotRule);

            System.out.println(url.getPath());

            if(userAgentRobotRule != null){
                for (String robotRule : userAgentRobotRule.getRuleList()){
                    String path = url.getPath();
                    if (robotRule.length() == 0) return true; // allows everything if BLANK
                    if (robotRule.equals("/")) return false;  // allows nothing if/
                    if (robotRule.length() <= path.length())
                    {
                        String pathCompare = path.substring(0, robotRule.length());
                        if (pathCompare.equals(robotRule)) return false;
                    }
                }
            }
        }
        return true;
    }

}
