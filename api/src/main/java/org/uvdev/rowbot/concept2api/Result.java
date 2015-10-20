package org.uvdev.rowbot.concept2api;

/**
 * Object containing an API result.
 */
public interface Result {

    /**
     * @return The status of the API operation. If {@link Concept2StatusCodes#OK} then the rest of
     *         the data is valid.
     */
    int getStatus();
}
