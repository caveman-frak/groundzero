# Import & Export

Evaluating the following CSV libraries

[Baeldung guide to CSV](https://www.baeldung.com/java-csv)

The following libraries have been evaludated so far:
[BeanIO](#beanio), [OpenCSV](#opencsv).

## BeanIO

[User Guide](https://beanio.github.io/)

[Github](https://github.com/beanio/beanio) last update 07 June 2024

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

## OpenCSV

[User Guide](https://opencsv.sourceforge.net/)

[SourceForge](https://sourceforge.net/p/opencsv/source/ci/master/tree/) last update 09 Sept 2022

Supports the following mappings:

* Annotations

Can unmarshal to either raw (String[]) or map to beans (but limited handling/validation if the bean is not annotated).

### Notes

Easy to configure parser, but validation and field handling is only supported via annotations.

Should be possible to extend the functionality to programmatically define field mapping/handling/validation but
would require, at the least, a custom mapping strategy and model extension to conveniently map requirements to the
field bindings without annotation support.

Does not support external mapping formats.

## Apache Commons CSV

## FastCSV

## Flatpack