package org.coode.parsers.oppl;

import org.coode.parsers.SymbolVisitorEx;
import org.coode.parsers.oppl.variableattribute.CollectionVariableAttributeSymbol;
import org.coode.parsers.oppl.variableattribute.StringVariableAttributeSymbol;

public interface OPPLSymbolVisitorEx<O> extends SymbolVisitorEx<O> {
	O visitStringVariableAttributeSymbol(StringVariableAttributeSymbol stringVariableAttributeSymbol);

	<P> O visitCollectionVariableAttributeSymbol(
			CollectionVariableAttributeSymbol<P> collectionVariableAttributeSymbol);

	O visitCreateOnDemandIdentifier(CreateOnDemandIdentifier createOnDemandIdentifier);
}
