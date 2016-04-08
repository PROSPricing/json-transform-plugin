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

package com.pros.jsontransform.plugin.constraint;

import com.fasterxml.jackson.databind.JsonNode;
import com.pros.jsontransform.ObjectTransformer;
import com.pros.jsontransform.ObjectTransformerException;

public class ConstraintTaxPercentRange extends ConstraintAbstract
{
    // plugins use the class name as transform directive
    private static String CONSTRAINT_NAME = "$com.pros.jsontransform.plugin.constraint.ConstraintTaxPercentRange";

    public static void validate(
        final JsonNode constraintNode,
        final JsonNode resultNode,
        final ObjectTransformer transformer)
    throws ObjectTransformerException
    {
        // expect price and tax nodes in sourceNode that is being processed
        JsonNode sourceNode = transformer.getSourceNode();
        double price = sourceNode.get("price").asDouble();
        double tax = sourceNode.get("tax").asDouble();
        double percentTax = tax * 100 / price;

        boolean ltRangeValid = true;
        boolean gtRangeValid = true;
        JsonNode rangeNode = constraintNode.get(CONSTRAINT_NAME);
        if (rangeNode.isObject())
        {
            JsonNode ltNode = rangeNode.path("$less-than");
            if (!ltNode.isMissingNode())
            {
                ltRangeValid = percentTax < ltNode.asDouble();
            }

            JsonNode gtNode = rangeNode.path("$greater-than");
            if (!gtNode.isMissingNode())
            {
                gtRangeValid = percentTax > gtNode.asDouble();
            }
        }

        if (ltRangeValid == false || gtRangeValid == false)
        {
            throw new ObjectTransformerException(
                "Constraint violation [" + CONSTRAINT_NAME + "]"
                    + " on transform node "
                    + transformer.getTransformNodeFieldName()
                    + " on result node "
                    + resultNode.toString()
                    + " "
                    + percentTax)
                    ;
        }
    }
}
