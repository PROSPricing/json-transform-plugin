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

package com.pros.jsontransform.plugin.sort;

import java.util.ArrayList;
import java.util.Comparator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pros.jsontransform.ObjectTransformer;
import com.pros.jsontransform.ObjectTransformerException;

public class ArraySortByAvgAge extends ArraySortAbstract
{
    // plugins use the class name as transform directive
    private static String PLUGIN_NAME = "$com.pros.jsontransform.plugin.sort.ArraySortByAvgAge";
    private static String SORT_DIRECTION = "$direction";
    private static String SORT_DIRECTION_ASC = "ascending";

    public static void sort(
        final ArrayNode arrayNode,
        final JsonNode sortNode,
        final ObjectTransformer transformer)
    throws ObjectTransformerException
    {
        // target array does not contain age information so
        // we need to use the source array from transformer context to perform the sort
        ArrayNode sourceArray = (ArrayNode)transformer.getSourceNode();

        // move source and target array nodes to sorting array
        int size = arrayNode.size();
        ArrayList<JsonNode> sortingArray = new ArrayList<JsonNode>(arrayNode.size());
        for (int i = 0; i < size; i++)
        {
            ObjectNode sortingElement = transformer.mapper.createObjectNode();
            sortingElement.put("source", sourceArray.get(i));
            sortingElement.put("target", arrayNode.remove(0));
            sortingArray.add(sortingElement);
        }

        // sort array
        sortingArray.sort(new AvgAgeComparator(sortNode));

        // move nodes back to targetArray
        for (int i = 0; i < sortingArray.size(); i++)
        {
            arrayNode.add(sortingArray.get(i).get("target"));
        }
    }

    /**
     * Comparator that compares sourceArrayNodes from sortingELements.
     */
    private static class AvgAgeComparator implements Comparator<JsonNode>
    {
        String sortDirection;

        AvgAgeComparator(JsonNode sortNode)
        {
            sortDirection = sortNode.get(PLUGIN_NAME).get(SORT_DIRECTION).asText();
        }

        @Override
        public int compare(JsonNode node1, JsonNode node2)
        {
            JsonNode sourceElement1 = node1.get("source");
            JsonNode sourceElement2 = node2.get("source");

            double avgAge1 = computeAvgAge(sourceElement1);
            double avgAge2 = computeAvgAge(sourceElement2);

            return sortDirection.equalsIgnoreCase(SORT_DIRECTION_ASC)
                ? Double.compare(avgAge1, avgAge2)
                : Double.compare(avgAge2, avgAge1)
                ;
        }

        private Double computeAvgAge(JsonNode familyArray)
        {
            double result = 0.0;

            // familyArray e.g. [{"name":"Smith"}, {"chris":50}, {"hanna":52}, {"paul":20}]
            if (familyArray.isArray())
            {
                int memberCount = 0;
                for (JsonNode familyMember : familyArray)
                {
                    JsonNode value = familyMember.elements().next();
                    if (value.isNumber())
                    {
                        result += value.asDouble();
                        memberCount++;
                    }
                }

                // average age
                if (memberCount > 0)
                {
                    result = result / memberCount;
                }
            }

            return result;
        }
    }

}
