package control.system.dagger;

import control.system.service.MappingService;
import control.system.service.MongoService;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ControlSystemModule.class)
public interface DependeniesComponent {

    MappingService getMappingService();

    MongoService getMongoServce();
}
