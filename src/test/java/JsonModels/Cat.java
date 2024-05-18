package JsonModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Cat {

    public String name;

    @JsonProperty("BodyParts")
    public ArrayList<String> bodyParts;

    public int age;

    public String getName() {
        return name;
    }

    public ArrayList<String> getBodyParts() {
        return bodyParts;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBodyParts(ArrayList<String> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public void setAge(int age) {
        this.age = age;
    }
}



