# Lehigh University Benchmark (LUBM)<br />Parallelized Artificial Data Generator
The Univ-Bench Artificial Data Generator (UBA) for the Lehigh University Benchmark (LUBM).
This is the original code for the generator rewritten to have a proper CLI and be much more scalable. While the generator here differs substantially from the original *all* changes have been implemented such that the data generated remains identical.

The provided  `verify` script in this repository will generate data using the original code plus the rewritten code (using a variety of the supported modes and output formats) and verifies that the generated data is identical.

## Usage
Requirement for usage is a Java JRE version 7 or higher.
Download a release archive or build the distribution (see `Build` section below).

Usage:

`./bin/dlubm options`

Usage summary:

`./bin/dlubm --help`

### Performance Tuning

There are a number of parameters that can be used to tune the performance of the generator.  The best combination will depend on the hardware on which you are generating the data.

#### Multi-threading

We strongly suggest using `--threads` to set the number of threads, typically you should set this to twice the number of processor cores (assuming hyper-threading enabled).  Using this option will give you substantially better performance than not using it.

#### Consolidation

Using consolidation will reduce the number of files generated though total IO will be roughly the same. With `--consolidate Partial` you get a file per university (which can still be a lot of files at scale) while `--consolidate Full` will produce a single file per-thread which provides the least number of files while still giving good parallel throughput.

Some data formats e.g. the property graph ones require producing single files in which case two pass writes are used to balance IO contention across threads. Each thread generates files which are then registered with a background thread which combines these into a final file.

There are other data formats such as N-Triples and Turtle where this additional consolidation maybe optionally enabled by setting `--consolidate Maximal`. When you use this is setting the generator will select an optimal consolidation mode to use for the data format in question and apply any necessary two pass consolidation.

#### Compression

The `--compress` option trades processing power for substantially reduced IO. The reduced IO is invaluable at larger scales, for example with 1000 universities and `--consolidate Full` the compressed N-Triples output file is 706 MB while the uncompressed output is 23 GB i.e. an approximately 32x compression ratio.

#### Output Format

The value given for `--format` controls the output data format and can have an effect on the amount of IO done and the performance.

`TURTLE` is the most compact format but is most expensive to produce because the reduction to prefixed name form takes extra time.  `NTRIPLES` and `OWL` are typically the fastest formats to produce.

#### Combining Compression and Consolidation

Whether combining `--consolidate` and `--compress` is worth it will depend on whether you are using a HDD or a SSD and perhaps more importantly the amount of free disk space you have since at large scales the data generated will be in the hundreds of gigabytes range uncompressed.

For example generating data like so:

`./bin/dlubm --quiet --timing -u 1000 --format NTRIPLES  --consolidate Full --threads 8`

Produces the follow performance numbers (on a quad core system with 4GB JVM Heap):

Disk | `--compress` | Time           | Total File Sizes (Approx.)
------ | ----------------- | --------------- | -----------
SSD | No                 | 188s          | 24 GB
SSD | Yes                |  233s         | 730 MB
HDD | No                | 343s           | 24 GB
HDD | Yes               | 392s           | 730 MB

Enabling compression increases overall time taken by roughly 25% on a SSD but by only 15% on a HDD.

For generating data like so:

`./bin/dlubm --quiet --timing -u 1000 --format NTRIPLES  --consolidate Partial --threads 8`

Produces the follow performance numbers (on a quad core system with 4GB JVM Heap):

Disk | `--compress` | Time           | Total File Sizes (Approx.)
------ | ----------------- | --------------- | -----------
SSD | No                 | 147s          | 24 GB
SSD | Yes                | 296s          | 730 MB
HDD | No                | 427s           | 24 GB
HDD | Yes               | 407s           | 730 MB

Enabling compression increases overall time taken by roughly 100% on an SSD but leaves it about the same with an HDD.

For example generating data like so:

`./bin/dlubm --quiet --timing -u 1000 --format NTRIPLES  --consolidate Maximal --threads 8`

Produces the follow performance numbers (on a quad core system with 4GB JVM Heap):

Disk | `--compress` | Time           | Total File Sizes (Approx.)
------ | ----------------- | --------------- | -----------
SSD | No                 | 156s          | 24 GB
SSD | Yes                | 379s          | 706 MB

Remember that you can use the `--output` option to specify where the data files are generated and thus control what kind of disk the data is written to, if you fail to specify this then files are generated in your working directory i.e. where you launched the generator from.

Note that at larger scales we would recommend enabling compression regardless because otherwise you are liable to exhaust disk space.


## Building
Requirement for building is a Java JDK version 7 or higher and a working Internet connection for the download of dependencies.
[Gradle](https://gradle.org/) is used for build management.

- Build the distribution archives:

`./gradlew clean build assembleDist`

- Build a local distribution:

`./gradlew clean build installDist`

- Change the directory to a local distribution:

`cd build/install/dlubm/`

- Use the local distribution directly:

`./build/install/dlubm/bin/dlubm options`

## Changes
- Code improvements
    - `generate.sh` script for launching
    - Refactor code to make it cleaner while keeping behaviour as-is
    - Use log4j for logging
    - Added support for N-Triples and Turtle format outputs
    - Added support for compressed output (GZip)
    - Use a proper command line parsing library that provides meaningful built in help and parsing errors
    - New command line options:
        - Added `-o <dir>`/`--output <dir>` option to control where generated data files are written
        - Added `--format <format>` option to control the output format, supports `OWL`, `DAML`, `NTRIPLES`, `TURTLE`, `GRAPHML`, `GRAPHML_NODESFIRST`, `NEO4J_GRAPHML` and `JSON`
            - The GraphML and JSON based formats are property graph encodings of the generated dataset
        - Added `--compress` option which compresses output files with GZip as they are generated
        - Added `--consolidate <mode>` option which controls how many files are generates.  `None` generates 1 file per university department, `Partial` generates 1 file per university and `Full` generates a file per thread.  `Maximal` tries to reduce the number of files as far as possible, exact number of files produces depends on the output format.
        - Added `-t <threads>`/`--threads <threads>` option to allow parallel data generation for better performance
        - Added `--quiet` option to reduce logging verbosity
        - Added `--max-time <minutes>` option to specify the maximum amount of time to allow data generation to run for before forcibly aborting
- Build management
    - Java dependency raised to version 7 or higher
    - Changed directory structure to be able to build with Gradle
    - Build management via Gradle (see `build` section and `gradle.build` file)
    - Distribution building via Gradle (see `build` section and `gradle.build` file)
    - Launch scripts generated by Gradle
    - Configuration for GitLab CI
    - Added useful dependencies
- Bug fixes
     - Use OS specific filename separator character
     - Check for errors when writing files

## Copyright / Contact

### Gradle Integration

- Felix Leif Keppmann <felix.leif@keppmann.de>

### Parallelized Version

- Rob Vesse <rvesse@dotnetrdf.org>
- For more information on the parallelized version, visit [https://github.com/rvesse/lubm-uba](https://github.com/rvesse/lubm-uba)

### Original Version

- The Semantic Web and Agent Technologies (SWAT) Lab, CSE Department, Lehigh University
- Yuanbo Guo <yug2@lehigh.edu>
- For more information about the benchmark, visit [http://swat.cse.lehigh.edu/projects/lubm](http://swat.cse.lehigh.edu/projects/lubm)

## Related Work

* GraphML extensions based upon work from [https://github.com/ssrangan/GraphBench](https://github.com/ssrangan/GraphBench) by Rangan Sukumar but adapted for parallel data generation.

