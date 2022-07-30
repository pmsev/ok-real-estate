package mappers

import ReAdGremlinConst.FIELD_DESCRIPTION
import ReAdGremlinConst.FIELD_LOCK
import ReAdGremlinConst.FIELD_SELLER_ID
import ReAdGremlinConst.FIELD_TITLE
import models.ReAd
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty

fun GraphTraversal<Vertex, Vertex>.addReAd(ad: ReAd): GraphTraversal<Vertex, Vertex>? =
    this
        .property(VertexProperty.Cardinality.single, FIELD_TITLE, ad.title)
        .property(VertexProperty.Cardinality.single, FIELD_DESCRIPTION, ad.description)
        .property(VertexProperty.Cardinality.single, FIELD_LOCK, ad.lock.asString())
        .property(
            VertexProperty.Cardinality.single,
            FIELD_SELLER_ID,
            ad.sellerId.asString()
        )
        .addReIntObject(ad.reIntObject)

