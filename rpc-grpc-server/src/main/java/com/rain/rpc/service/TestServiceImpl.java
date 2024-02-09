package com.rain.rpc.service;

import com.rain.rpc.TestProto;
import com.rain.rpc.TestServiceGrpc;
import io.grpc.stub.StreamObserver;

public class TestServiceImpl extends TestServiceGrpc.TestServiceImplBase {
    @Override
    public void testRuns(TestProto.TestRequest request, StreamObserver<TestProto.TestResponse> responseObserver) {
        String name = request.getName();
        System.out.println("name = " + name);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        responseObserver.onNext(TestProto.TestResponse.newBuilder().setResult("this is result").build());
        responseObserver.onCompleted();
    }
}
