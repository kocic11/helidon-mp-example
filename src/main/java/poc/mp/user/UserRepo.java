package poc.mp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepo {
    private ConcurrentMap<String, User> repo = new ConcurrentHashMap<>();
    private AtomicInteger idGenerator = new AtomicInteger(0);

    private String getNextId() {
        return String.format("%04d", idGenerator.incrementAndGet());
    }

    public String add(User user) {
        String id = getNextId();
        user.setId(id);
        repo.put(id, user);
        return id;
    }

    public User getById(String id) {
        return repo.get(id);
    }

    public List<User> getByName(String name) {
        List<User> users = repo.values().stream().filter(user -> user.getName().equals(name))
                .collect(Collectors.toList());
        return users;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        users.addAll(repo.values());
        return users;
    }

    public void deleteAll() {
        repo = new ConcurrentHashMap<>();
    }
}