package me.sunxc.model;

public class User implements java.io.Serializable {
    
    private int id;
    private String name;
    
    
    public User() {
    }

    public User(String name) { 
        this.name = name;
    }

    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + "]";
    }
    
    
}
