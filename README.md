# HDFS to S3

TO-DO

## Contributing to the project

### Configuring your environment

TO-DO

### Building and testing this project with specific Java version (using [jenv](https://www.jenv.be/))

This command will run the tests with enabled coverage as configured in [build.sbt](build.sbt):

```shell script
jenv exec sbt clean test
```

#### To generate the coverage reports run

```shell script
jenv exec sbt coverageReport
```

### Listing dependencies of this project

```shell script
jenv exec sbt dependencyTree
```

```shell script
jenv exec sbt dependencyBrowseGraph
```

## Building distribution from source code

```shell script
jenv exec sbt universal:packageBin
```

## Useful Hadoop commands

```shell script
/usr/local/hadoop/bin/hadoop fs -mkdir /dir
```

```shell script
/usr/local/hadoop/bin/hadoop fs -put /etc/sudo.conf /dir/file.txt
```

## Implementation details

### Hadoop DistCP (distributed copy)

[Hadoop DistCP](https://hadoop.apache.org/docs/current/hadoop-distcp/DistCp.html)

### Hadoop S3A client

__Authentication:__

This problem is explained in the [Bucket restrictions and limitations](https://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html):

> For best compatibility, we recommend that you avoid using dots (.) in bucket names, except for buckets that are used only for static website hosting. If you include dots in a bucket's name, you can't use virtual-host-style addressing over HTTPS, unless you perform your own certificate validation. This is because the security certificates used for virtual hosting of buckets don't work for buckets with dots in their names.

A more detailed explanation with examples given in: [Using Virtual Host URLs with HTTPS](https://shlomoswidler.com/2009/08/18/amazon-s3-gotcha-using-virtual-host/).

__Writing data to S3:__

By default, rely on disk buffering.

## Troubleshooting

* [Troubleshooting hadoop-aws s3a](https://hadoop.apache.org/docs/current/hadoop-aws/tools/hadoop-aws/troubleshooting_s3a.html).
