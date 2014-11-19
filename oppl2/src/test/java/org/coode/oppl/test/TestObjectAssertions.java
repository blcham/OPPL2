package org.coode.oppl.test;

import java.io.File;
import java.net.URL;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import uk.ac.manchester.cs.jfact.JFactFactory;

@SuppressWarnings("javadoc")
public class TestObjectAssertions {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        OWLOntologyManager ontologyManager = OWLManager
                .createOWLOntologyManager();
        URL url = new URL(args[0]);
        File parentFile = new File(url.toURI()).getParentFile();
        if (parentFile != null && parentFile.isDirectory()) {
            AutoIRIMapper mapper = new AutoIRIMapper(parentFile, true);
            ontologyManager.getIRIMappers().add(mapper);
        }
        OWLOntology ontology = ontologyManager.loadOntology(IRI.create(url));
        JFactFactory factory = new JFactFactory();
        OWLReasoner reasoner = factory.createReasoner(ontology);
        ontology.individualsInSignature().forEach(
                i -> ontology.objectPropertiesInSignature().forEach(
                        p -> getOPValue(reasoner, i, p)));
    }

    protected static void getOPValue(OWLReasoner reasoner,
            OWLNamedIndividual i, OWLObjectProperty p) {
        long start = System.currentTimeMillis();
        reasoner.getObjectPropertyValues(i, p);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("TestObjectAssertions.main() " + elapsed);
    }
}
