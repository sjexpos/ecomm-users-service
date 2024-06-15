package io.oigres.ecomm.service.users.textsearch;

import org.hibernate.search.mapper.pojo.bridge.RoutingBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.RoutingBridgeRouteContext;
import org.hibernate.search.mapper.pojo.route.DocumentRoutes;

import io.oigres.ecomm.service.users.domain.Auditable;

public class AuditableRoutingBridge implements RoutingBridge<Auditable> {

    @Override
    public void route(DocumentRoutes routes, Object entityIdentifier, Auditable indexedEntity, RoutingBridgeRouteContext context) {
        if (indexedEntity.getDeletedAt() == null) {
            routes.addRoute();
        } else {
            routes.notIndexed();
        }
    }

    @Override
    public void previousRoutes(DocumentRoutes routes, Object entityIdentifier, Auditable indexedEntity, RoutingBridgeRouteContext context) {
        routes.addRoute();        
    }
    
}
