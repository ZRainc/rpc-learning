package com.rain.rpc.service;

import com.rain.rpc.HelloProto;
import com.rain.rpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        String name = request.getName();
        System.out.println("Name is " + name);

        responseObserver.onNext(HelloProto.HelloResponse.newBuilder().setResult("result").build());
        responseObserver.onCompleted();
    }
}
