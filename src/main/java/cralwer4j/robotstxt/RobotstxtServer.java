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

package cralwer4j.robotstxt;

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
            //logger.error("Bad URL in Robots.txt: " + webURL, e);
        }

        //logger.warn("RobotstxtServer: default: allow", webURL);
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

}
