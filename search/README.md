# Set project
gcloud config set project stelinno-dev

# Google App Engine Standard Environment Search API Sample

This sample demonstrates how to use App Engine Search API.

See the [Google App Engine Search API documentation][search-api-docs] for more
detailed instructions.

[search-api-docs]: https://cloud.google.com/appengine/docs/java/search/

# Search Package API
https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/search/package-summary

## Setup
* `gcloud init`

## Running locally
    $ mvn appengine:run

## Deploying
    $ mvn appengine:deploy
    
## URI's

`/search` | Create a Search API Document
`/search/index` | Index a document 
`/search/delete` | Delete a search document
`/search/option` | Search w/ query options
`/search/search` | Search for a document
`/search/schema` | Display the schema of a document
