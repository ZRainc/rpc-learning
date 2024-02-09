package com.rain.rpc;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// FuntureStub

public class GrpcClient7 {
    public static void main(String[] args) {
        // 1. 创建通信的管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        // 2. 获得代理对象 stub
        try {
            TestServiceGrpc.TestServiceFutureStub testService = TestServiceGrpc.newFutureStub(managedChannel);
            ListenableFuture<TestProto.TestResponse> responseListenableFuture = testService.testRuns(TestProto.TestRequest.newBuilder().setName("Joyce").build());

            // 同步的操作
//            TestProto.TestResponse testResponse = responseListenableFuture.get();
//            System.out.println(testResponse.getResult());

//            responseListenableFuture.addListener(() -> {
//                System.out.println("异步的rpc响应 回来了。。。。");
//            }, Executors.newCachedThreadPool());

            Futures.addCallback(responseListenableFuture, new FutureCallback<TestProto.TestResponse>() {
                @Override
                public void onSuccess(TestProto.TestResponse result) {
                    System.out.println("result.getResult() = " + result.getResult());
                }

                @Override
                public void onFailure(Throwable t) {

                }
            }, Executors.newCachedThreadPool());

            System.out.println("后续的操作");

            managedChannel.awaitTermination(12, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            managedChannel.shutdown();
        }
    }
}
