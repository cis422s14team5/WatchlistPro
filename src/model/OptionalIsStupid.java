package model;

import java.util.Optional;


public class OptionalIsStupid {
    private Optional<String> optional;

    public String getOptional() {
        return optional.get();
    }

    public void setOptional(Optional<String> optional) {
        this.optional = optional;
    }

}
