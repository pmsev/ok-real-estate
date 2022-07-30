package mappers

import ReAdGremlinConst.FIELD_ADDRESS
import ReAdGremlinConst.FIELD_DISTRICT
import ReAdGremlinConst.FIELD_LOCATION_LATITUDE
import ReAdGremlinConst.FIELD_LOCATION_LONGITUDE
import ReAdGremlinConst.FIELD_PRICE
import ReAdGremlinConst.FIELD_ROOMS
import ReAdGremlinConst.FIELD_SQUARE
import models.ReIntObject
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty

fun GraphTraversal<Vertex, Vertex>.addReIntObject(reIntObject: ReIntObject): GraphTraversal<Vertex, Vertex> =
        property(VertexProperty.Cardinality.single, FIELD_SQUARE, reIntObject.square)
            .property(VertexProperty.Cardinality.single, FIELD_PRICE, reIntObject.price)
            .property(VertexProperty.Cardinality.single, FIELD_ROOMS, reIntObject.rooms)
            .property(VertexProperty.Cardinality.single, FIELD_DISTRICT, reIntObject.district)
            .property(VertexProperty.Cardinality.single, FIELD_ADDRESS, reIntObject.address)
            .property(VertexProperty.Cardinality.single, FIELD_LOCATION_LATITUDE, reIntObject.location.latitude.asDouble())
            .property(VertexProperty.Cardinality.single, FIELD_LOCATION_LONGITUDE, reIntObject.location.longitude.asDouble())