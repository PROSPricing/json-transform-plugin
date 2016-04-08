/*
 * Copyright (c) 2016 PROS, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.pros.jsontransform.plugin.expression;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pros.jsontransform.ObjectTransformer;
import com.pros.jsontransform.ObjectTransformerException;

public class FunctionAvgChildrenAge extends FunctionAbstract
{
    public static JsonNode evaluate(
        final JsonNode argsNode,
        final JsonNode resultNode,
        final ObjectTransformer transformer)
    throws ObjectTransformerException
    {
        double avg = 0.0;

        // argument $value indicates children array in source node context
        if (argsNode.get("$value") != null)
        {
            // transformer resolves $value to corresponding node
            JsonNode childrenArray = transformer.transformValueNode(
                transformer.getSourceNode(), argsNode);
            if (childrenArray.isArray())
            {
                for (JsonNode child : childrenArray)
                {
                    avg += child.path("age").asDouble();
                }
                avg = avg / childrenArray.size();
            }
        }

        // use argsNode to hold temporary return value node
        ObjectNode holderNode = (ObjectNode)argsNode;
        holderNode.put("returnValue", avg);

        return holderNode.get("returnValue");
    };
}
