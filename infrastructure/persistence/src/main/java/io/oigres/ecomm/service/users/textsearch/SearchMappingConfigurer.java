package io.oigres.ecomm.service.users.textsearch;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.User;

@Component("searchMappingConfigurer")
public class SearchMappingConfigurer implements HibernateOrmSearchMappingConfigurer {

    @Override
    public void configure(HibernateOrmMappingConfigurationContext context) {
        ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();
        configureUserIndexes(mapping);
    }

    private void configureUserIndexes(ProgrammaticMappingConfigurationContext mapping) {
        TypeMappingStep userMapping = mapping.type(User.class);
        userMapping.indexed()
            .routingBinder(new AuditableRoutingBinder());
        userMapping.property("id")
                .documentId();
        userMapping.property("email")
                .fullTextField();
        userMapping.property("email")
                .keywordField("email_sorted")
                .sortable(Sortable.YES);
    }

}
