# json-transform-plugin

This project contains few examples of json-transform plugins. json-transform can be extended by writing Java plugins for the following transform directives:

- **$constraints** ([code]
  (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/main/java/com/pros/jsontransform/plugin/constraint)
  [examples]
  (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/test/java/com/pros/jsontransform/plugin/constraint))
- **$expression** ([code]
  (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/main/java/com/pros/jsontransform/plugin/expression)
  [examples]
  (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/test/java/com/pros/jsontransform/plugin/expression))
- **$include / $exclude** ([code]
  (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/main/java/com/pros/jsontransform/plugin/filter)
  [examples]
  (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/test/java/com/pros/jsontransform/plugin/filter))
- **$sort** ([code]
  (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/main/java/com/pros/jsontransform/plugin/sort)
  [examples]
  (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/test/java/com/pros/jsontransform/plugin/sort))

## How Plugins Work

As an example, the [**FunctionAvgChildrenAge**] (https://stash.pros.com/users/lsuardi/repos/json-transform-plugin/browse/src/main/java/com/pros/jsontransform/plugin/expression/FunctionAvgChildrenAge.java) plugin to the **$expression** directive works on a *children* array object with *age* information and calculates the average children age.

**The source JSON**

```javascript
{
    "person":
    {
        "firstName":"Peter",
        "lastName":"Smith",
        "age":33,
        "children":
        [
            {"name":"Chris", "age":10},
            {"name":"Helen", "age":13}
        ],
        "carCount": 2,
        "boatCount": 0
    }
}
```

**The transform map**

See how the plugin is activated as part of the **$expression** directive. The plugin works in the context of the *ObjectTransformer*, which in this case is the root source object. The typical format for specifying plugins in the JSON transform map is as follows:

{"$plugin.class.name":{"$arg1":"...", "$arg2":123, ... }}

```javascript
{
    "fullName":
    {
        "$value":"person|firstName",
        "$expression":
        [
            {"$append":{"$what":" "}},
            {"$append":{"$what":{"$value":"person|lastName"}}}
        ]
    },
    "avgChildrenAge":
    {
        "$expression":
        [
            {
                "$com.transform.plugin.expression.FunctionAvgChildrenAge":
                {
                    "$value":"person|children"
                }
            }
        ]
    }
}
```

**The target JSON**

The result of the transformation process is the target JSON.

```javascript
{
  "fullName" : "Peter Smith",
  "avgChildrenAge" : 11.5
}
```

## How To Use

After plugins are deployed to a suitable plugin folder, the ObjectTransformer is configured to load the  plugin Java classes and perform the corresponding plugin functions.

```java
ObjectMapper mapper = new ObjectMapper();
Properties properties = new Properties();
properties.setProperty("plugin.folder", "true");
ObjectTransformer transformer = new ObjectTransformer(properties, mapper);
JsonNode result = transformer.transform(jsonSource, jsonTransform);
```

## How To Build

Before importing the project in your favourite IDE, building the source with **gradle** is recommended to retrieve the necessary dependencies as well as verify integrity of the code (unit tests). A prerequisite is that json-transform has been built with gradle and the corresponding jar file is available to the json-transform-plugin build. It may be necessary to modify the *gradle/build.gradle* to indicate the correct location of the json-transform.jar artifact. From the project root folder at the command line:

- cd gradle
- gradlew build

The gradle build follows the standard gradle java plugin setup:

- *gradle/build/classes* contains the compiled source
- *gradle/build/libs* contains the json-transform-plugin.jar
- *\<user home\>/.gradle/caches/modules-2/files-2.1* contains the jar files json-transform-plugin depends on


