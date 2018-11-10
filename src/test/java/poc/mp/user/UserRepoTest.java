package poc.mp.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserRepoTest {

    private static UserRepo userRepo = new UserRepo();
    private static User user;
    private static String id;
    private static List<User> expected = new ArrayList<>();

    public static void main(String[] args) {
        UserRepoTest test = new UserRepoTest();
        test.addUsers();
        test.testGetAll();
        test.testDeleteAll();
    }

    @BeforeEach
    public void addUsers() {
        user = new User("Xyz", 18);
        userRepo.add(user);
        expected.add(user);
        user = new User("Abc", 18);
        id = userRepo.add(user);
        expected.add(user);
        expected.sort(null);
    }

    @Test
    public void testGetById() {
        User actual = userRepo.getById(id);
        Assertions.assertTrue(user.equals(actual), "Users should be equal");
    }

    @Test
    public void testGetByName() {
        List<User> actual = userRepo.getByName("Abc");
        Assertions.assertTrue(user.equals(actual.get(0)), "Users should be equal");
    }

    @Test
    public void testGetAll() {
        List<User> actual = userRepo.getAll();
        Assertions.assertTrue(actual.toArray().length > 0, "Size should be greater tha 0");
    }

    @Test
    public void testDeleteAll() {
        userRepo.deleteAll();
        Assertions.assertTrue(userRepo.getAll().size() == 0, "Size should be 0");
    }
}