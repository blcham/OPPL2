package org.coode.oppl;

import java.util.stream.Stream;

import org.coode.oppl.utils.OWLObjectExtractor;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Utils for streams.
 */
public class StreamUtils {

    /**
     * @param onts
     *        ontologies
     * @return annotation properties
     */
    public static Stream<OWLAnnotationProperty> getAllAnnotationProperties(
            Stream<OWLOntology> onts) {
        return onts.flatMap(o -> o.annotationPropertiesInSignature());
    }

    /**
     * @param onts
     *        ontologies
     * @return classes
     */
    public static Stream<OWLClass> getAllClasses(Stream<OWLOntology> onts) {
        return onts.flatMap(o -> o.classesInSignature());
    }

    /**
     * @param onts
     *        ontologies
     * @return literals
     */
    public static Stream<OWLLiteral> getAllConstants(Stream<OWLOntology> onts) {
        return onts.flatMap(o -> o.axioms()).flatMap(
                ax -> OWLObjectExtractor.getAllOWLLiterals(ax).stream());
    }

    /**
     * @param onts
     *        ontologies
     * @return data properties
     */
    public static Stream<OWLDataProperty> getAllDataProperties(
            Stream<OWLOntology> onts) {
        return onts.flatMap(o -> o.dataPropertiesInSignature());
    }

    /**
     * @param onts
     *        ontologies
     * @return individuals
     */
    public static Stream<OWLIndividual> getAllIndividuals(
            Stream<OWLOntology> onts) {
        return onts.flatMap(o -> o.individualsInSignature());
    }

    /**
     * @param onts
     *        ontologies
     * @return object properties
     */
    public static Stream<OWLObjectProperty> getAllObjectProperties(
            Stream<OWLOntology> onts) {
        return onts.flatMap(o -> o.objectPropertiesInSignature());
    }
}
