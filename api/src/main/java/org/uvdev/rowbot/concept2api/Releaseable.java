package org.uvdev.rowbot.concept2api;

/**
 * Used if the object contains a reference to memory that must be released.
 */
public interface Releaseable {

    /**
     * Release the associated memory.
     */
    void release();
}
