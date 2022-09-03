package com.example.kb_2022;

public class Photo_Type {
    private int Path;
    private String Name;

    public int get_Photo_Path() {
        return Path;
    }

    public String get_Photo_Name() {
        return Name;
    }

    public void set_Photo_Name(String name) {
        Name = name;
    }

    public void set_Photo_Path(int path) {
        Path = path;
    }
}
