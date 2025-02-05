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

package io.oigres.ecomm.service.users.orm;

import io.oigres.ecomm.service.users.domain.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CustomAuditingEntityListener {
  private ObjectFactory<AuditingHandler> handler;

  public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> auditingHandler) {
    this.handler = auditingHandler;
  }

  @PrePersist
  public void touchForCreate(Object target) {
    Assert.notNull(target, "Entity must not be null!");
    if (handler != null) {
      AuditingHandler object = handler.getObject();
      if (object != null) {
        object.markCreated(target);
      }
    }
  }

  @PreUpdate
  public void touchForUpdate(Object target) {
    Assert.notNull(target, "Entity must not be null!");
    if (handler != null) {
      AuditingHandler object = handler.getObject();
      if (object != null) {
        object.markModified(target);
        if (Auditable.class.isAssignableFrom(target.getClass())
            && ((Auditable) target).isDeleted()
            && ((Auditable) target).getDeletedBy() == null) {
          ((Auditable) target).setDeletedBy(((Auditable) target).getModifiedBy());
        }
      }
    }
  }
}
