#### Basic information
Plugin supports two types of definitions: definition of Models and definition of Endpoints.

Definition of models can be either a definition of Enum or a definition of a full-sized model.

### Structure of an Enum

```
enum <ENUM_NAME> {
    <ENUM_VALUE_1>
    <ENUM_VALUE_2>
    ...
    <ENUM_VALUE_N>
}
```

#### Detailed description of an enum parts
* *ENUM_NAME*: name of the enum.
* *ENUM_VALUE_N*: A possible value of an enum.

### Structure of a Model

```
model <MODEL_NAME> : <PARENT_MODEL_NAME>{
    <PROPERTY_1_NAME>: <PROPERTY_1_MODEL_NAME>
    <PROPERTY_2_NAME>: <PROPERTY_2_MODEL_NAME>
    ...
    <PROPERTY_N_NAME>: <PROPERTY_N_MODEL_NAME>
}
```

#### Detailed description of a model parts:
* MODEL_NAME: name of the model.
* *PROPERTY_N_NAME*: name of the property that this model contains.
* *PROPERTY_N_MODEL_NAME*: name of the model of the corresponding property that this model contains.
* *PARENT_MODEL_NAME*: name of the parent model (standart inheritance).

#### Generic arguments
Models can have generic arguments, so the following model is correct:

```
model Test {
        test: List<Integer>
    }
```

*PARENT_MODEL_NAME* is an optional block



### Definition of an Endpoint

```
<METHOD> <PATH> {
    request: <REQUEST_MODEL_NAME>
    response: <RESPONSE_MODEL_NAME>
    params {
        <PARAM_1_NAME>: <PARAM_1_MODEL_NAME>
        <PARAM_2_NAME>: <PARAM_2_MODEL_NAME>
        ...
        <PARAM_N_NAME>: <PARAM_N_MODEL_NAME>
    }
}
```

#### Detailed description of an endpoint parts:
* METHOD : REST method (can be one of [GET, PUT, POST, DELETE, OPTION]).
* PATH: endpoint path as it is declared in API. Example: `/accounts/{accountId}/orders/books/notes`
* REQUEST_MODEL_NAME: name of the model that is accepted by this endpoint as body.
* RESPONSE_MODEL_NAME: name of the model that is returned by this endpoint.
* PARAM_N_NAME: name of the request param. Example: `accountId`.
* PARAM_N_MODEL_NAME: name of the model of the corresponding request param.

*request, response and params and their corresponding parts are optional blocks.*

#### Predefined models
There are some predefined models, which are included in plugin definition:
* Integer
* String
* Date
* DateTime
* Boolean
* Double
* BigInteger
* BigDecimal
* List
* Set
* CriteriaBase *(project-specific)*
* Object
* Map
* T (also known as generic argument)
* Array
* File


#### Comments

Comments are supported in both files in any place. syntax: /\*COMMENT*/


#### Main functions of a plugin itself:

* models and endpoints navigation via ‘go to symbol’
* ‘go to definition’ on models
* rename refactoring
* find usages (looks for both usages of model and all parent models)
* structure view
* syntax highlighting
* autocomplete
* comment
* syntax and non-syntax error highlight
* reformat
