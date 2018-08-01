/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package crawler4j.robotstxt;

import org.apache.http.NoHttpResponseException;

import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yasser Ganjisaffar
 */
public class RobotstxtServer {

    //private static final Logger logger = LoggerFactory.getLogger(RobotstxtServer.class);

    protected RobotstxtConfig config;

    private final Map<String, HostDirectives> host2directivesCache = new HashMap<>();

    private final int maxBytes;

    public RobotstxtServer(RobotstxtConfig config) {
        this(config, 16384);
    }

    public RobotstxtServer(RobotstxtConfig config, int maxBytes) {
        this.config = config;
        this.maxBytes = maxBytes;
    }

    private static String getHost(URL url) {
        return url.getHost().toLowerCase();
    }

    /** Please note that in the case of a bad URL, TRUE will be returned */
    public boolean allows(String webURL) {
        if (!config.isEnabled()) {
            return true;
        }
        try {
            URL url = new URL(webURL);
            String host = getHost(url);
            String path = url.getPath();

            HostDirectives directives = host2directivesCache.get(host);

            if (directives != null && directives.needsRefetch()) {
                synchronized (host2directivesCache) {
                    host2directivesCache.remove(host);
                    directives = null;
                }
            }
            if (directives == null) {
                directives = fetchDirectives(url);
            }
            return directives.allows(path);
        } catch (MalformedURLException e) {
            //logger.error("Bad URL in Robots.txt: " + webURL.getURL(), e);
        }

        //logger.warn("RobotstxtServer: default: allow", webURL.getURL());
        return true;
    }

    private HostDirectives fetchDirectives(URL url) {
        String host = getHost(url);
        String port = ((url.getPort() == url.getDefaultPort()) || (url.getPort() == -1)) ? "" :
                (":" + url.getPort());
        String proto = url.getProtocol();
        String robotsUrl = proto + "://" + host + port + "/robots.txt";
        HostDirectives directives = null;
        try {
            URL urlRobot = new URL(robotsUrl);
            InputStream urlRobotStream = urlRobot.openStream();
            byte b[] = new byte[1024];
            int numRead;
            StringBuilder sb = new StringBuilder();
            while((numRead=urlRobotStream.read(b))!=-1){
                String newCommands = new String(b, 0, numRead);
                sb.append(newCommands);
            }
            String content = sb.toString();
            directives = RobotstxtParser.parse(content, config);
            urlRobotStream.close();
        }catch (MalformedURLException | SocketException | UnknownHostException | SocketTimeoutException |
                NoHttpResponseException se) {
            // No logging here, as it just means that robots.txt doesn't exist on this server
            // which is perfectly ok
            //logger.trace("robots.txt probably does not exist.", se);
        } catch (Exception e) {
            //logger.error("Error occurred while fetching (robots) url: " + robotsTxtUrl.getURL(), e);
        }

        if (directives == null) {
            // We still need to have this object to keep track of the time we fetched it
            directives = new HostDirectives(config);
        }
        synchronized (host2directivesCache) {
            if (host2directivesCache.size() == config.getCacheSize()) {
                String minHost = null;
                long minAccessTime = Long.MAX_VALUE;
                for (Map.Entry<String, HostDirectives> entry : host2directivesCache.entrySet()) {
                    long entryAccessTime = entry.getValue().getLastAccessTime();
                    if (entryAccessTime < minAccessTime) {
                        minAccessTime = entryAccessTime;
                        minHost = entry.getKey();
                    }
                }
                host2directivesCache.remove(minHost);
            }
            host2directivesCache.put(host, directives);
        }
        return directives;
    }

    /*private HostDirectives fetchDirectives(URL url) {
        WebURL robotsTxtUrl = new WebURL();
        String host = getHost(url);
        String port = ((url.getPort() == url.getDefaultPort()) || (url.getPort() == -1)) ? "" :
                      (":" + url.getPort());
        String proto = url.getProtocol();
        robotsTxtUrl.setURL(proto + "://" + host + port + "/robots.txt");
        HostDirectives directives = null;
        PageFetchResult fetchResult = null;
        try {
            for (int redir = 0; redir < 3; ++redir) {
                fetchResult = pageFetcher.fetchPage(robotsTxtUrl);
                int status = fetchResult.getStatusCode();
                // Follow redirects up to 3 levels
                if ((status == HttpStatus.SC_MULTIPLE_CHOICES ||
                     status == HttpStatus.SC_MOVED_PERMANENTLY ||
                     status == HttpStatus.SC_MOVED_TEMPORARILY ||
                     status == HttpStatus.SC_SEE_OTHER ||
                     status == HttpStatus.SC_TEMPORARY_REDIRECT || status == 308) &&
                    // SC_PERMANENT_REDIRECT RFC7538
                    fetchResult.getMovedToUrl() != null) {
                    robotsTxtUrl.setURL(fetchResult.getMovedToUrl());
                    fetchResult.discardContentIfNotConsumed();
                } else {
                    // Done on all other occasions
                    break;
                }
            }

            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
                Page page = new Page(robotsTxtUrl);
                // Most recent answer on robots.txt max size is
                // https://goo.gl/OqpKbP
                fetchResult.fetchContent(page, 10_000 * 1024);
                if (Util.hasPlainTextContent(page.getContentType())) {
                    String content;
                    if (page.getContentCharset() == null) {
                        content = new String(page.getContentData());
                    } else {
                        content = new String(page.getContentData(), page.getContentCharset());
                    }
                    directives = RobotstxtParser.parse(content, config);
                } else if (page.getContentType()
                               .contains(
                                   "html")) { // TODO This one should be upgraded to remove all
                    // html tags
                    String content = new String(page.getContentData());
                    directives = RobotstxtParser.parse(content, config);
                } else {
                    //logger.warn("Can't read this robots.txt: {}  as it is not written in plain text, " + "contentType: {}", robotsTxtUrl.getURL(), page.getContentType());
                }
            } else {
                //logger.debug("Can't read this robots.txt: {}  as it's status code is {}",robotsTxtUrl.getURL(), fetchResult.getStatusCode());
            }
        } catch (SocketException | UnknownHostException | SocketTimeoutException |
            NoHttpResponseException se) {
            // No logging here, as it just means that robots.txt doesn't exist on this server
            // which is perfectly ok
            //logger.trace("robots.txt probably does not exist.", se);
        } catch (PageBiggerThanMaxSizeException pbtms) {
            //logger.error("Error occurred while fetching (robots) url: {}, {}", robotsTxtUrl.getURL(), pbtms.getMessage());
        } catch (Exception e) {
            //logger.error("Error occurred while fetching (robots) url: " + robotsTxtUrl.getURL(), e);
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }

        if (directives == null) {
            // We still need to have this object to keep track of the time we fetched it
            directives = new HostDirectives(config);
        }
        synchronized (host2directivesCache) {
            if (host2directivesCache.size() == config.getCacheSize()) {
                String minHost = null;
                long minAccessTime = Long.MAX_VALUE;
                for (Map.Entry<String, HostDirectives> entry : host2directivesCache.entrySet()) {
                    long entryAccessTime = entry.getValue().getLastAccessTime();
                    if (entryAccessTime < minAccessTime) {
                        minAccessTime = entryAccessTime;
                        minHost = entry.getKey();
                    }
                }
                host2directivesCache.remove(minHost);
            }
            host2directivesCache.put(host, directives);
        }
        return directives;
    }*/
}
