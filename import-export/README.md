# Import & Export

Evaluating the following CSV libraries

[Baeldung guide to CSV](https://www.baeldung.com/java-csv)

The following libraries have been evaluated so far:
[BeanIO](#beanio), [OpenCSV](#opencsv), [ApacheCSV](#apache-commons-csv),
[FastCSV](#fastcsv), [CSVeed](#csveed).

## BeanIO

[User Guide](https://beanio.github.io/)

[GitHub](https://github.com/beanio/beanio) last update 07 June 2024

Gradle: `implementation("com.github.beanio:beanio:3.0.2")`

Supports the following mappings and formats:

* XML Mapping File
* Annotations
* Programmatic mapping
* Multiple formats (CSV/XML/delimited/fixed length)

Can unmarshal to either raw (map or collection) as well as mapping to beans.

Supports complex mappings:

* Span multiple records
* Repeating fields and segments
* Multiple records types
* Record identification
    * by marker field
    * by length
    * by field count
* Record ordering and grouping

### Notes

Powerful, yet easy to use parser. Supports the usual field and record level validation and includes error message
localization.

Supports creating immutable beans or Java records using the field definitions, but does not directly support either
factory constructors or [Lombok](https://projectlombok.org/) builders.

Adding headers to output is easy but not obvious (need to create a header record definition with defaults for each
field).

#### Cons:

* Has a bug around handling annotated collection fields.

## OpenCSV

[User Guide](https://opencsv.sourceforge.net/)

[SourceForge](https://sourceforge.net/p/opencsv/source/ci/master/tree/) last update 09 Sept 2022

Gradle: `implementation("com.opencsv:opencsv:5.9")`

Supports the following mappings:

* Annotations

Can unmarshal to either raw (String[]) or map to beans (but limited handling/validation if the bean is not annotated).

### Notes

Easy to configure parser, but validation and field handling is only supported via annotations.

Should be possible to extend the functionality to programmatically define field mapping/handling/validation but
would require, at the least, a custom mapping strategy and model extension to conveniently map requirements to the
field bindings without annotation support.

#### Con:

* Does not support external mapping formats.
* Default collection mapping expects space delimited, can only be overridden with annotations.
* Bug with collection handling for empty string (creates collections with a "" entry, rather than empty)

## Apache Commons CSV

[User Guide](https://commons.apache.org/proper/commons-csv/user-guide.html)

[GitHub](https://github.com/apache/commons-csv) last updated Jun 11 2024

Gradle: `implementation("org.apache.commons:commons-csv:1.11.0")`

## FastCSV

[User Guide](https://fastcsv.org/)

[GitHub](https://github.com/osiegmar/FastCSV) last updated Jun 15 2024

Gradle: `implementation("de.siegmar:fastcsv:3.2.0")`

## Flatpack

[User Guide](https://flatpack.sourceforge.net/)

[GitHub](https://github.com/appendium/flatpack) last release 27 Dec 2021

Gradle: `implementation("net.sf.flatpack:flatpack:4.0.18")`

## CSVeed

[User Guide](https://42bv.github.io/CSVeed/csveed.html)

[GitHub](https://github.com/42BV/CSVeed) last updated May 28 2024

Gradle: `implementation("org.csveed:csveed:0.8.1")`