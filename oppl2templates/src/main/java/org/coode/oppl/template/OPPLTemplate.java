package org.coode.oppl.template;

/** A template is a String with place-holders. Replacing such place holders with
 * the appropriate String will produce an instanceof the parameter O.
 * 
 * @author Luigi Iannone
 * @param <O> */
public interface OPPLTemplate<O> {
    /** Retrieves the template String.
     * 
     * @return A String with place-holders. */
    String getTemplateString();

    /** Performs the replacement.
     * 
     * @return an instance of O if the replacement did not incur in parsing
     *         errors. <code>null</code> otherwise. */
    O replace();
}
