package com.rain.rpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

// 异步的方式处理消息

public class GrpcClient4 {
    public static void main(String[] args) {
        // 1. 创建通信的管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        // 2. 获得代理对象 stub
        try {
            HelloServiceGrpc.HelloServiceStub helloService = HelloServiceGrpc.newStub(managedChannel);
            HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
            builder.setName("eric");
            HelloProto.HelloRequest helloRequest = builder.build();


            helloService.c2ss(helloRequest, new StreamObserver<HelloProto.HelloResponse>() {
                @Override
                public void onNext(HelloProto.HelloResponse helloResponse) {
                    // 服务端 响应了 一个消息后，需要立即处理的话，把代码写在这个方法中
                    System.out.println("每一次响应的信息 " + helloResponse.getResult());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    // 需要把服务端 相应的所有数据 拿到后，在进行业务处理
                    System.out.println("服务端响应结束，后续可以根据需要 在这里统一处理服务端响应的所有内容");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            managedChannel.shutdown();
        }
    }
}
