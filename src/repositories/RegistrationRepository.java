package repositories;

import entities.Registration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistrationRepository implements Repository<Registration> {
    private List<Registration> registrations = new ArrayList<>();

    @Override
    public Registration create(Registration registration) {
        registrations.add(registration);
        return registration;
    }

    @Override
    public Optional<Registration> read(int id) {
        return registrations.stream()
                            .filter(registration -> registration.getId() == id)
                            .findFirst();
    }

    @Override
    public List<Registration> readAll() {
        return new ArrayList<>(registrations);
    }

    @Override
    public Registration update(Registration registration) {
        int index = registrations.indexOf(read(registration.getId()).orElse(null));
        if (index >= 0) {
            registrations.set(index, registration);
            return registration;
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        return registrations.removeIf(registration -> registration.getId() == id);
    }
}
