package org.coode.oppl.generated;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.ManchesterVariableSyntax;
import org.coode.oppl.Variable;
import org.coode.oppl.VariableVisitor;
import org.coode.oppl.VariableVisitorEx;
import org.coode.oppl.function.OPPLFunction;
import org.coode.oppl.variabletypes.VariableType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObject;

/** @author Luigi Iannone
 * @param <O>
 *            type */
public class GeneratedVariable<O extends OWLObject> implements Variable<O> {
    private final OPPLFunction<? extends O> opplFunction;
    private final String name;
    private final IRI iri;
    private final VariableType<O> type;

    protected GeneratedVariable(String name, VariableType<O> type,
            OPPLFunction<? extends O> opplFunction) {
        this.name = checkNotNull(name, "name");
        this.type = checkNotNull(type, "type");
        this.opplFunction = checkNotNull(opplFunction, "opplFunction");
        this.iri = IRI.create(ManchesterVariableSyntax.NAMESPACE + this.getName());
    }

    @Override
    public String render(ConstraintSystem constraintSystem) {
        return String.format("%s:%s = %s", this.getName(), this.getType(), this
                .getOPPLFunction().render(constraintSystem));
    }

    @Override
    public String toString() {
        return String.format("%s:%s", this.getName(), this.getType());
    }

    /** @return the opplFunction */
    public OPPLFunction<? extends O> getOPPLFunction() {
        return this.opplFunction;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IRI getIRI() {
        return this.iri;
    }

    @Override
    public VariableType<O> getType() {
        return this.type;
    }

    @Override
    public void accept(VariableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T accept(VariableVisitorEx<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        result = prime * result + (this.type == null ? 0 : this.type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        GeneratedVariable<?> other = (GeneratedVariable<?>) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }

    /** @param name
     *            name
     * @param type
     *            type
     * @param opplFunction
     *            opplFunction
     * @param <P>
     *            variable type
     * @return generated variable */
    public static <P extends OWLObject> GeneratedVariable<P> getGeneratedVariable(
            String name, VariableType<P> type, OPPLFunction<? extends P> opplFunction) {
        return new GeneratedVariable<>(name, type, opplFunction);
    }
}
