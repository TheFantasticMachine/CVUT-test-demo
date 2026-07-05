package com.testgen.demo.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Settings {

    @JsonProperty("remember_me")
    public boolean rememberMe;
    @JsonProperty("user_data")
    public String[] userData;

    @JsonProperty("save_last_tests")
    public int saveLastTests;
}
