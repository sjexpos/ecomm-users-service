package io.oigres.ecomm.service.users.textsearch;

import org.hibernate.search.mapper.pojo.bridge.binding.RoutingBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.RoutingBinder;

import io.oigres.ecomm.service.users.domain.Auditable;

public class AuditableRoutingBinder implements RoutingBinder {

    @Override
    public void bind(RoutingBindingContext context) {
        context.dependencies()
                .use(Auditable.DELETED_AT_PROPERTY_NAME);
        context.bridge(
                Auditable.class,
                new AuditableRoutingBridge());

    }

}
