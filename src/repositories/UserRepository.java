package repositories;

import entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<User> {
    private List<User> users = new ArrayList<>();

    @Override
    public User create(User user) {
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> read(int id) {
        return users.stream()
                    .filter(user -> user.getId() == id)
                    .findFirst();
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User update(User user) {
        int index = users.indexOf(read(user.getId()).orElse(null));
        if (index >= 0) {
            users.set(index, user);
            return user;
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        return users.removeIf(user -> user.getId() == id);
    }
    // Method to get a user by email
    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user; 
            }
        }
        return null; 
    }
}
