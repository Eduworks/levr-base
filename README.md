levr-base
=========

Releases can be found at the following site: http://build.eduworks.com/dist/levr/

LEVR is a web service framework that allows you to write and publish complex web services quickly and in a variety of domain-specific languages.

This product contains a number of Resolvers and Crunchers, the core executing components of LEVR, that can be used along with one of the LEVR Languages (like RS2) to produce web services.

* Couchbase Crunchers
* MapDB Crunchers (CRUD)
* Couchdb Resolvers (CRUD)
* Caching Crunchers
* File Operation Crunchers (zip, unzip, exists, load, save, base64 encoding, etc)
* Simple Language Crunchers (Call, count, distribute, keyset, valueset, toObject, etc)
* Collection Manipulation Crunchers (Append, Remove, Pivot, Intersect, Union)
* Simple Math Crunchers (Add, Max, Avg, RMS, Top, Sum)
* TTP Crunchers (Get, Post)
* Reflection (Levr Introspection) Crunchers (Commit RS2, Manifest)
* Paypal Crunchers
* SOLR Crunchers
* Text To Speech Crunchers
* Time Manipulation Crunchers
* UUID Generation Cruncher
* Parsing/Pretty Printing HTML/XML Crunchers
* XMPP Client Crunchers
* File Parsing Crunchers (Raw text to String, requires text/plain)
* Scoreboard Resolvers (for Michiko)
* Display Resolvers (output as JSON, XML, CSV, JSONML)
* Simple Language Resolvers (Foreach, List, Error)
* Manipulation Resolvers (Flatten, Reduce, Filter)
* Math Resolvers (King of the Hill, Scoring, Divide, Subtract, Round)
* Net Resolvers (Get File from URL, Harvest OAI, Harvest RSS)
* Encryption Resolvers (BCrypt, AES)
* Services Resolvers (OAuth, LR Publish, Send Email, Validate Paypal)
* String Resolvers (Hashcode, String operations)
* Date Resolvers (Date formatting)
* Scripters (JS, Python)
* LEVRAGE Web Development Environment

levr-base is Open Source under the Apache 2.0 license, the details of which can be found in license.txt.

levr-base is under active development. It is released only as part of other projects, and you will not find JAR releases here. If this is desired, contact one of our developers.

levr-base contains support for the following languages, all of which will be represented as examples in levr-scripts (eventually).

* RS2
* Javascript
* Python

levr-base requires the following to build or use:

* Java 1.6 or above. (1.6.0_26 recommended)
* ANT (for building)
* IVY (for fetching libraries from Maven)
* eduworks-common, found at https://github.com/Eduworks/eduworks-common
* levr-core, found at https://github.com/Eduworks/levr-core

The bug tracker for levr-base is being currently maintained on GitHub, at the following url: https://github.com/Eduworks/levr-base/issues

The lead contact for levr-base is Fritz (fritz.ray@eduworks.com).
