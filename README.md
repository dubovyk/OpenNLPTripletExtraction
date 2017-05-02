# OpenNLPTripletExtraction

This project allow to enter a text and get triplets of entity-relations [Subject] - [Verb] - [Object] from it.

## Installation

Read the installation_guide.pdf for complete compile&install guide.

## Under the hood

This tool uses OpenNLP framework which does the job on parsing text and building syntax trees. Then it simply uses it to get ER`s and remove data which is not required by the user. Then it stores them intro neo4j database.

## License

This tool is published under the MIT License, so feel free to use, modify or update it as you like.