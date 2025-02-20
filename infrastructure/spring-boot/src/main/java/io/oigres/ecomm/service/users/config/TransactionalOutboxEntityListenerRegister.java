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

import io.oigres.ecomm.service.users.txoutbox.TransactionalOutboxPatternEntityListener;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TransactionalOutboxEntityListenerRegister {

  private final SessionFactory sessionFactory;
  private final TransactionalOutboxPatternEntityListener listener;

  public TransactionalOutboxEntityListenerRegister(
      SessionFactory sessionFactory, TransactionalOutboxPatternEntityListener listener) {
    this.sessionFactory = sessionFactory;
    this.listener = listener;
  }

  @PostConstruct
  public void registerListeners() {
    SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
    ServiceRegistryImplementor serviceRegistry = sessionFactoryImpl.getServiceRegistry();
    EventListenerRegistry registry = serviceRegistry.getService(EventListenerRegistry.class);
    if (registry != null) {
      registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(listener);
      registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(listener);
      registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(listener);
    } else {
      log.error("It was not possible to register TransactionalOutboxPatternEntityListener!");
    }
  }
}
