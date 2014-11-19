package org.coode.oppl.entity;

import java.util.Optional;

import org.semanticweb.owlapi.model.OWLEntity;

/** @author Luigi Iannone */
public class OWLEntityRendererImpl implements OWLEntityRenderer {

    @Override
    public String render(OWLEntity entity) {
        try {
            Optional<String> rendering = entity.getIRI().getRemainder();
            if (!rendering.isPresent()) {
                // Get last bit of path
                String path = entity.getIRI().toURI().getPath();
                if (path == null) {
                    return entity.getIRI().toString();
                }
                return entity.getIRI().toURI().getPath()
                        .substring(path.lastIndexOf("/") + 1);
            }
            return RenderingEscapeUtils.getEscapedRendering(rendering.get());
        } catch (Exception e) {
            return "<Error! " + e.getMessage() + ">";
        }
    }
}
