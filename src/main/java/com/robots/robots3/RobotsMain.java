package com.robots.robots3;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * User-agent: * 这里的*代表的所有的搜索引擎种类，*是一个通配符
 * Disallow: /admin/ 这里定义是禁止爬寻admin目录下面的目录
 * Disallow: /require/ 这里定义是禁止爬寻require目录下面的目录
 * Disallow: /ABC/ 这里定义是禁止爬寻ABC目录下面的目录
 * Disallow: /cgi-bin/*.htm 禁止访问/cgi-bin/目录下的所有以".htm"为后缀的URL(包含子目录)。
 * Disallow: /*?* 禁止访问网站中所有的动态页面
 * Disallow: /jpg$ 禁止抓取网页所有的.jpg格式的图片
 * Disallow:/ab/adc.html 禁止爬去ab文件夹下面的adc.html文件。
 * Allow: /cgi-bin/　这里定义是允许爬寻cgi-bin目录下面的目录
 * Allow: /tmp 这里定义是允许爬寻tmp的整个目录
 * Allow: .htm$ 仅允许访问以".htm"为后缀的URL。
 * Allow: .gif$ 允许抓取网页和gif格式图片
 * Crawl-delay: 10
 */
public class RobotsMain {

    private static final String DISALLOW = "Disallow";
    private static final String ALLOW = "Allow";

    public static void main(String[] args){
        String taoBaoUrl = "https://www.taobao.com/nanzhuang";
        String weiboUrl = "https://www.weibo.com/robots.txt";
        String renrenUrl = "http://www.people.com.cn/robots.txt";
        String zhihuUrl = "https://www.zhihu.com/login/";
        String baiduUrl = "https://www.baidu.com/link";
        String googleUrl = "https://www.google.com/robots.txt";

        String userAgent = "Googlebot";
        System.out.println(isAllow(taoBaoUrl, userAgent));
    }

    private static boolean isAllow(String strUrl, String userAgent)
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

        System.out.println(strCommands);

        if (strCommands.contains(ALLOW) || strCommands.contains(DISALLOW)) // if there are no "disallow" values, then they are not blocking anything.
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
                        robotRules.add(r);
                    }
                } else if (line.startsWith(ALLOW)) {
                    if (mostRecentUserAgent != null) {
                        r.setUserAgent("userAgent: " + mostRecentUserAgent);
                        int start = line.indexOf(":") + 1;
                        int end = line.length();
                        r.setRule(ALLOW + ": "+line.substring(start, end).trim());
                    }
                }else if (line.startsWith(DISALLOW)) {
                    if (mostRecentUserAgent != null) {
                        r.setUserAgent("userAgent: " + mostRecentUserAgent);
                        int start = line.indexOf(":") + 1;
                        int end = line.length();
                        r.setRule(DISALLOW + ": "+line.substring(start, end).trim());
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
                    if(robotRule.startsWith(ALLOW)){
                        int start = robotRule.indexOf(":") + 1;
                        int end =robotRule.length();
                        robotRule = robotRule.substring(start,end).trim();
                        if(path.startsWith(robotRule)){
                            return true;
                        }
                    }else if(robotRule.startsWith(DISALLOW)){
                        int start = robotRule.indexOf(":") + 1;
                        int end =robotRule.length();
                        robotRule = robotRule.substring(start,end).trim();
                        if (robotRule.length() == 0) return true; // allows everything if BLANK
                        if (robotRule.equals("/")) return false;  // allows nothing if/
                        if (robotRule.length() <= path.length()) {
                            String pathCompare = path.substring(0, robotRule.length());
                            if (pathCompare.equals(robotRule)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
