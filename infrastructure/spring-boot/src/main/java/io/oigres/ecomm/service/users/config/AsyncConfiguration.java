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

package io.oigres.ecomm.service.users.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

  @Bean(name = "asyncPoolTaskExecutor")
  public ThreadPoolTaskExecutor executor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    // Core thread count.
    taskExecutor.setCorePoolSize(10);
    // The maximum number of threads maintained in the thread pool. Only when the buffer queue is
    // full will threads exceeding the core thread count be requested.
    taskExecutor.setMaxPoolSize(100);
    // Cache queue.
    taskExecutor.setQueueCapacity(50);
    // Allowed idle time. Threads other than core threads will be destroyed after the idle time
    // arrives.
    taskExecutor.setKeepAliveSeconds(200);
    // Thread name prefix for asynchronous methods.
    taskExecutor.setThreadNamePrefix("async-");
    /**
     * When the task cache queue of the thread pool is full and the number of threads in the thread pool reaches maximumPoolSize, if there are still tasks coming, a task rejection policy will be adopted.
     * There are usually four policies:
     * ThreadPoolExecutor.AbortPolicy: Discard the task and throw RejectedExecutionException.
     * ThreadPoolExecutor.DiscardPolicy: Also discard the task, but do not throw an exception.
     * ThreadPoolExecutor.DiscardOldestPolicy: Discard the task at the front of the queue and then try to execute the task again (repeat this process).
     * ThreadPoolExecutor.CallerRunsPolicy: Retry adding the current task and automatically call the execute() method repeatedly until it succeeds.
     */
    taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    taskExecutor.initialize();
    return taskExecutor;
  }
}
