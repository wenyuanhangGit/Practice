package edu.uci.ics.crawler4j;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args){
        CrawlConfig crawlConfig = new CrawlConfig();
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setUserAgentName("*");
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,pageFetcher);

        String taobaoUrl = "https://www.taobao.com/robots.txt";//淘宝
        String toutiaoUrl = "https://www.toutiao.com/robots.txt";//头条
        String baijiahaoUrl = "http://baijiahao.baidu.com/robots.txt";//百家号
        String jiemianUrl = "https://www.jiemian.com/robots.txt";//界面文章
        String chinazUrl = "http://www.chinaz.com/robots.txt";//站长之家
        String digiTechUrl = "http://digi.tech.qq.com/robots.txt";//腾讯数码新闻--都可以爬
        String ifengUrl = "http://www.ifeng.com/robots.txt";//凤凰网--都可以爬
        String wallstreetcnUrl = "https://wallstreetcn.com/robots.txt";//华尔街见闻--都可以爬 robots.txt 404 Not Found
        String tmallUrl = "https://www.tmall.com/robots.txt";//天猫
        String jdUrl = "https://www.jd.com/robots.txt";//京东
        String amazonUrl = "https://www.amazon.cn/robots.txt";//亚马逊
        String _1688Url = "https://www.1688.com/gongsi/robots.txt";//1688
        String dianpingUrl = "https://www.dianping.com/robots.txt";//大众点评
        String meituanUrl = "https://www.meituan.com/robots.txt";//美团
        String zhihuUrl = "https://www.zhihu.com/robots.txt";//知乎
        String baikeUrl = "https://baike.baidu.com/robots.txt";//百度百科
        String ctripUrl = "http://www.ctrip.com/service/robots.txt";//携程
        String anjukeUrl = "https://hangzhou.anjuke.com/robots.txt";//安居客
        String youkuUrl = "https://www.youku.com/video/robots.txt";//优酷--没有robots.txt
        String bilibiliUrl = "https://www.bilibili.com/robots.txt";//哔哩哔哩
        String music163Url = "https://music.163.com/robots.txt";//网易云音乐--没有robots.txt

        WebURL webURL = new WebURL();
        webURL.setURL(taobaoUrl);
        boolean flag = robotstxtServer.allows(webURL);

        System.out.println(flag);
    }

}
