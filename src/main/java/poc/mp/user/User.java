package poc.mp.user;

import java.lang.Comparable;

public class User implements Comparable<User> {
    private String id;
    private String name;
    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object u) {

        if (u == null)
            return false;

        if (u instanceof User) {
            return (this.id.equals(((User) u).id) && this.name.equals(((User) u).name) && (this.id == ((User) u).id));
        }

        return false;

    }

    @Override
    public int compareTo(User o) {
        return this.name.compareTo(o.name);
    }

}