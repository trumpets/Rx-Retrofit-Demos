package com.citycollege.sdmd.reactiveprogramming;

import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("age")
    private String age;

    // Necessary for JSON serialization
    public Student() {

    }

    public Student(String firstName, String lastName, String age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    @Override
    public String toString() {
        // This is called by the ArrayAdapter class to get a textual description of the item
        return firstName + " " + lastName;
    }
}
