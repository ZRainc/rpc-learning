package com.rain.rpc.service;

import com.google.protobuf.ProtocolStringList;
import com.rain.rpc.HelloProto;
import com.rain.rpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    /*
    * 1. 接收client提交的参数
    * 2. 业务处理 service + dto 调用对应的业务功能
    * 3. 提供返回值
    * */

    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        // 1. 接收客户端的请求参数
        String name = request.getName();
        // 2. 业务处理
        System.out.println("name parameters " + name);
        // 3. 封装响应
        // 3.1 创建相应对象的构造者
        HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
        // 3.2 填充数据
        builder.setResult("hello method invoke ok");
        // 3.3 封装响应
        HelloProto.HelloResponse helloResponse = builder.build();

        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }


    @Override
    public void hello1(HelloProto.HelloRequest1 request, StreamObserver<HelloProto.HelloResponse1> responseObserver) {
        ProtocolStringList nameList = request.getNameList();
        for (String s: nameList) {
            System.out.println("s = " + s);
        }

        HelloProto.HelloResponse1.Builder builder = HelloProto.HelloResponse1.newBuilder();
        builder.setResult("ok");
        HelloProto.HelloResponse1 helloResponse1 = builder.build();

        responseObserver.onNext(helloResponse1);
        responseObserver.onCompleted();
    }

    @Override
    public void c2ss(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        // 1. 接收client的请求参数
        String name = request.getName();
        // 2. 做业务处理
        System.out.println("name = " + name);
        // 3. 根据业务处理的结果，提供响应
        for (int i = 0; i < 9; i++) {
            HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
            builder.setResult("处理的结果 " + i);
            HelloProto.HelloResponse helloResponse = builder.build();

            responseObserver.onNext(helloResponse);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }
}
