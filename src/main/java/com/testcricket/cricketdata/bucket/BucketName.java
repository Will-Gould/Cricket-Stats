package com.testcricket.cricketdata.bucket;

public enum BucketName {

    TEST_MATCHES("cricket-tests");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
