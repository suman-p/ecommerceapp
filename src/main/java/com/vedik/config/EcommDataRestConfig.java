package com.vedik.config;

import com.vedik.entity.Country;
import com.vedik.entity.Product;
import com.vedik.entity.ProductCategory;
import com.vedik.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class EcommDataRestConfig implements RepositoryRestConfigurer{

    private EntityManager entityManager;

    public EcommDataRestConfig(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        //disable HTTP methods for Product: PUT, POST, DELETE
        disableHttpMethods(Product.class, config, cors);
        //disable HTTP methods for ProductCategory: PUT, POST, DELETE
        disableHttpMethods(ProductCategory.class, config, cors);
        //disable HTTP methods for Country: PUT, POST, DELETE
        disableHttpMethods(Country.class, config, cors);
        //disable HTTP methods for State: PUT, POST, DELETE
        disableHttpMethods(State.class, config, cors);

        exposeIds(config);
    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, CorsRegistry corsRegistry){
        HttpMethod[] unsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedActions)));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //expose entity ids
        //

        //get list of all entity classes from entity manager
        Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();

        //create array of entity types
        List<Class> entityClasses = new ArrayList<>();

        //Get Entity types for entities
        for(EntityType entityType: entityTypes){
            entityClasses.add(entityType.getJavaType());
        }

        //expose entity ids for entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);

    }
}
