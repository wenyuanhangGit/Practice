package cralwer4j;

import cralwer4j.robotstxt.RobotstxtConfig;
import cralwer4j.robotstxt.RobotstxtServer;

public class Main {

    public static void main(String[] args){
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setUserAgentName("*");
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig);

        String taobaoUrl = "https://www.taobao.com/robots.txt";//淘宝
        String toutiaoUrl = "https://www.toutiao.com/robots.txt";//头条
        String baijiahaoUrl = "http://baijiahao.baidu.com/u?app_id=*";//百家号
        String jiemianUrl = "https://www.jiemian.com/robots.txt";//界面文章
        String chinazUrl = "http://www.chinaz.com/robots.txt";//站长之家
        String digiTechUrl = "http://digi.tech.qq.com/robots.txt";//腾讯数码新闻--都可以爬
        String ifengUrl = "http://www.ifeng.com/robots.txt";//凤凰网--都可以爬
        String wallstreetcnUrl = "https://wallstreetcn.com/robots.txt";//华尔街见闻--都可以爬 robots.txt 404 Not Found
        String tmallUrl = "https://www.tmall.com/mlist.html";//天猫
        String jdUrl = "https://www.jd.com/robots.txt";//京东
        String amazonUrl = "https://www.amazon.cn/robots.txt";//亚马逊
        String _1688Url = "https://www.1688.com/yijiandaifa/*?*t";//1688
        String dianpingUrl = "https://www.dianping.com/robots.txt";//大众点评
        String meituanUrl = "https://www.meituan.com/robots.txt";//美团
        String zhihuUrl = "https://www.zhihu.com/robots.txt";//知乎
        String baikeUrl = "https://baike.baidu.com/robots.txt";//百度百科
        String ctripUrl = "http://www.ctrip.com/service/robots.txt";//携程
        String anjukeUrl = "https://hangzhou.anjuke.com/robots.txt";//安居客
        String youkuUrl = "https://www.youku.com/video/robots.txt";//优酷--没有robots.txt
        String bilibiliUrl = "https://www.bilibili.com/robots.txt";//哔哩哔哩
        String music163Url = "https://music.163.com/robots.txt";//网易云音乐--没有robots.txt

        String sohuUrl = "http://www.sohu.com/robots.txt";

        boolean flag = robotstxtServer.allows(sohuUrl);
        System.out.println(flag);

        /*flag = robotstxtServer.allows(taobaoUrl);
        System.out.println(flag);

        flag = robotstxtServer.allows(tmallUrl);
        System.out.println(flag);

        flag = robotstxtServer.allows(meituanUrl);
        System.out.println(flag);*/
    }

}
