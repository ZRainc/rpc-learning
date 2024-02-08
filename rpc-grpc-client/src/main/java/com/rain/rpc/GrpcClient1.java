package com.rain.rpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient1 {
    public static void main(String[] args) {
        // 1. 创建通信的管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        // 2. 获得代理对象 stub
        try {
            HelloServiceGrpc.HelloServiceBlockingStub helloService = HelloServiceGrpc.newBlockingStub(managedChannel);
            // 3. 完成RPC调用
            // 3.1 准备参数
            HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
            builder.setName("eric");
            HelloProto.HelloRequest helloRequest = builder.build();
            // 3.2 进行功能 rpc 调用， 获取响应数据
            HelloProto.HelloResponse helloResponse = helloService.hello(helloRequest);
            String result = helloResponse.getResult();
            System.out.println("result = " + result);
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            managedChannel.shutdown();
        }
    }
}
