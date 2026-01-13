package com.priyanshu.resumegenerator;

public class Resume {
    public String name;
    public java.util.List<String> skills;
    public java.util.List<Project> projects;  // Changed to Project objects
}

class Project {
    public String name;
    public String description;

    @Override
    public String toString() {
        return (name != null ? name : "Unnamed Project") +
                (description != null ? " - " + description : "");
    }
}
