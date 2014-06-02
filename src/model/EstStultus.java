package model;

import java.util.Optional;

/**
 * An implementation of Optional that allows the user to access it.
 */
public class EstStultus {

    private Optional<String> optional;

    /**
     * Get the optional.
     * @return the optional.
     */
    public String getOptional() {
        return optional.get();
    }

    /**
     * Set the optional.
     * @param optional is the optional to set.
     */
    public void setOptional(Optional<String> optional) {
        this.optional = optional;
    }
}
