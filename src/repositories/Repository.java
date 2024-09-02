package repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<Entity> {
    Entity create(Entity object);
    Optional<Entity> read(int id);
    List<Entity> readAll();
    Entity update(Entity object);
    boolean delete(int id);
}
