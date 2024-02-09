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

    /*
    * 客户端流式RPC
    * */
    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2s(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            @Override
            public void onNext(HelloProto.HelloRequest helloRequest) {
                System.out.println("接收到了client发送一条消息 " + helloRequest.getName());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Client的所有消息 都发送到了 服务端 ...");
                // 提供响应：响应目的，当接收了全部client提交的数据，并处理后，提供响应
                HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
                builder.setResult("this is result");
                HelloProto.HelloResponse helloResponse = builder.build();
                responseObserver.onNext(helloResponse);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2ss(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            @Override
            public void onNext(HelloProto.HelloRequest helloRequest) {
                System.out.println("接收client 提交的数据" + helloRequest.getName());
                responseObserver.onNext(HelloProto.HelloResponse.newBuilder().setResult("response " + helloRequest.getName() + " result ").build());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("接收到了所有的请求消息。。。。");
                responseObserver.onCompleted();
            }
        };
    }
}
