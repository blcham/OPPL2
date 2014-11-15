package org.coode.parsers.oppl.testcase.protege;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.coode.oppl.protege.ui.ShowMessageRuntimeExceptionHandler;
import org.coode.parsers.common.SystemErrorEcho;
import org.coode.parsers.oppl.testcase.OPPLTestCase;
import org.coode.parsers.oppl.testcase.OPPLTestCaseParser;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.AnnotationContainer;
import org.semanticweb.owlapi.model.*;

/** @author Luigi Iannone */
public class OPPLTestCaseAnnotationContainer implements AnnotationContainer {
    private final OWLEditorKit owlEditorKit;
    private final OWLAnnotationProperty testCaseAnnotationProperty;
    protected final OPPLTestCaseParser parser;

    /** @param owlEditorKit
     *            owlEditorKit */
    public OPPLTestCaseAnnotationContainer(OWLEditorKit owlEditorKit) {
        this.owlEditorKit = checkNotNull(owlEditorKit, "owlEditorKit");
        testCaseAnnotationProperty = Preferences
                .getTestCaseAnnotationProperty(getOWLEditorKit().getOWLModelManager()
                        .getOWLDataFactory());
        ProtegeParserFactory parserFactory = new ProtegeParserFactory(getOWLEditorKit());
        parser = parserFactory.build(new SystemErrorEcho());
    }

    @Override
    public Set<OWLAnnotation> getAnnotations() {
        OWLOntology activeOntology = getOWLEditorKit().getOWLModelManager()
                .getActiveOntology();
        Set<OWLAnnotation> toReturn = activeOntology.getAnnotations();
        Iterator<OWLAnnotation> iterator = toReturn.iterator();
        while (iterator.hasNext()) {
            OWLAnnotation annotation = iterator.next();
            OPPLTestCase extracted = getOPPLTestCase(annotation);
            if (extracted == null) {
                iterator.remove();
            }
        }
        return toReturn;
    }

    /** @param annotation
     *            annotation
     * @return test case */
    public OPPLTestCase getOPPLTestCase(OWLAnnotation annotation) {
        OPPLTestCase extracted = null;
        if (annotation.getProperty().equals(testCaseAnnotationProperty)) {
            OWLAnnotationValue value = annotation.getValue();
            extracted = value.accept(new OWLObjectVisitorEx<OPPLTestCase>() {
                @Override
                public OPPLTestCase visit(OWLLiteral literal) {
                    String input = literal.getLiteral();
                    OPPLTestCase parsed = parser.parse(input,
                            new ShowMessageRuntimeExceptionHandler(
                                    OPPLTestCaseAnnotationContainer.this
                                            .getOWLEditorKit().getOWLWorkspace()));
                    return parsed;
                }
            });
        }
        return extracted;
    }

    /** @return test cases */
    public Set<OPPLTestCase> getOPPLTestCases() {
        Set<OWLAnnotation> annotations = getAnnotations();
        Set<OPPLTestCase> toReturn = new HashSet<OPPLTestCase>(annotations.size());
        for (OWLAnnotation owlAnnotation : annotations) {
            toReturn.add(getOPPLTestCase(owlAnnotation));
        }
        return toReturn;
    }

    /** @return the owlEditorKit */
    public OWLEditorKit getOWLEditorKit() {
        return owlEditorKit;
    }

    /** @return the ontology */
    public OWLOntology getOntology() {
        return getOWLEditorKit().getOWLModelManager().getActiveOntology();
    }
}
