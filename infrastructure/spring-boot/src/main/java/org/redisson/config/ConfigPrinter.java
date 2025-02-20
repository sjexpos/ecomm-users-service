/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package org.redisson.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.cache.CacheException;
import org.redisson.hibernate.RedissonRegionFactory;
import org.slf4j.Logger;

public class ConfigPrinter {

  public void print(Logger log, String configPath) {
    Config config = loadRedissonConfig(configPath);
    if (config.isClusterConfig()) {
      ClusterServersConfig clusterServersConfig = config.getClusterServersConfig();
      log.info("Client name: {}", clusterServersConfig.getClientName());
      log.info("Nodes: {}", clusterServersConfig.getNodeAddresses());
      log.info("Username: {}", clusterServersConfig.getUsername());
      String rawPassword = clusterServersConfig.getPassword();
      String password =
          rawPassword.substring(0, 1)
              + StringUtils.repeat("*", rawPassword.length() - 2)
              + rawPassword.substring(rawPassword.length() - 1);

      log.info("Password: {}", password);
      log.info("Connection timeout: {}", clusterServersConfig.getConnectTimeout());
      log.info("Timeout: {}", clusterServersConfig.getTimeout());
      log.info(
          "Master connection pool size: {}", clusterServersConfig.getMasterConnectionPoolSize());
      log.info("Slave connection pool size: {}", clusterServersConfig.getSlaveConnectionPoolSize());
      log.info("Retry attempts: {}", clusterServersConfig.getRetryAttempts());
    } else if (config.getSingleServerConfig() != null) {
      SingleServerConfig singleServerConfig = config.getSingleServerConfig();
      log.info("Client name: {}", singleServerConfig.getClientName());
      log.info("Address: {}", singleServerConfig.getAddress());
      log.info("Username: {}", singleServerConfig.getUsername());
      log.info("Password: {}", singleServerConfig.getPassword());
      log.info("Connection timeout: {}", singleServerConfig.getConnectTimeout());
      log.info("Connection pool size: {}", singleServerConfig.getConnectionPoolSize());
      log.info("Timeout: {}", singleServerConfig.getTimeout());
      log.info("Retry attempts: {}", singleServerConfig.getRetryAttempts());
    }
  }

  protected Config loadRedissonConfig(String configPath) {
    Config config = null;
    if (StringUtils.isBlank(configPath)) {
      config = loadConfig(RedissonRegionFactory.class.getClassLoader(), "redisson.json");
      if (config == null) {
        config = loadConfig(RedissonRegionFactory.class.getClassLoader(), "redisson.yaml");
      }
    } else {
      config = loadConfig(RedissonRegionFactory.class.getClassLoader(), configPath);
      if (config == null) {
        config = loadConfig(configPath);
      }
    }
    if (config == null) {
      throw new CacheException("Unable to locate Redisson configuration");
    }
    return config;
  }

  private Config loadConfig(String configPath) {
    Path path = Path.of(configPath);
    try {
      return Config.fromYAML(path.toFile());
    } catch (IOException e) {
      // trying next format
      try {
        return Config.fromJSON(path.toFile());
      } catch (IOException e1) {
        throw new CacheException("Can't parse default yaml config", e1);
      }
    }
  }

  private Config loadConfig(ClassLoader classLoader, String fileName) {
    InputStream is = classLoader.getResourceAsStream(fileName);
    if (is != null) {
      try {
        return Config.fromYAML(is);
      } catch (IOException e) {
        try {
          is = classLoader.getResourceAsStream(fileName);
          return Config.fromJSON(is);
        } catch (IOException e1) {
          throw new CacheException("Can't parse yaml config", e1);
        }
      }
    }
    return null;
  }
}
