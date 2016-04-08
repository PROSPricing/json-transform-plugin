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

package com.pros.jsontransform.plugin.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.pros.jsontransform.ObjectTransformer;

public class ArrayFilterTaxPercentRange
{
    // plugins use the class name as transform directive
    private static String CONSTRAINT_NAME = "$com.pros.jsontransform.plugin.filter.ArrayFilterTaxPercentRange";

    public static boolean evaluate(
        final JsonNode filterNode,
        final JsonNode elementNode,
        final ObjectTransformer transformer)
    {
        // expect price and tax nodes in elementNode
        double price = elementNode.get("price").asDouble();
        double tax = elementNode.get("tax").asDouble();
        double percentTax = tax * 100 / price;

        boolean ltRangeValid = true;
        boolean gtRangeValid = true;
        JsonNode rangeNode = filterNode.get(CONSTRAINT_NAME);
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
        
        return ltRangeValid && gtRangeValid;
    }

}
