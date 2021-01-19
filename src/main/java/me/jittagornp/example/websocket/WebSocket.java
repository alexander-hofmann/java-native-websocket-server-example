/*
 * Copyright 2021-Current jittagornp.me
 */
package me.jittagornp.example.websocket;

import me.jittagornp.example.util.ByteBufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author jitta
 */
public class WebSocket {

    private String sessionId;

    private final SocketChannel channel;

    private final FrameDataByteBufferConverter converter;

    private boolean isHandshake;

    public WebSocket(final SocketChannel channel) {
        this.channel = channel;
        this.sessionId = UUID.randomUUID().toString();
        this.converter = new FrameDataByteBufferConverterImpl();
    }

    public String getSessionId() {
        return sessionId;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public boolean isHandshake() {
        return isHandshake;
    }

    public void setHandshake(final boolean handshake) {
        isHandshake = handshake;
    }

    public void send(final String text) {
        final ByteBuffer payloadData = ByteBufferUtils.create(text);
        send(payloadData, Opcode.TEXT_FRAME);
    }

    public void send(final ByteBuffer payloadData) {
        send(payloadData, Opcode.BINARY_FRAME);
    }

    public void send(final ByteBuffer payloadData, final Opcode opcode) {
        final FrameData frameData = FrameData.builder()
                .fin(true)
                .rsv1(false)
                .rsv2(false)
                .rsv3(false)
                .opcode(opcode)
                .mask(false)
                .payloadData(payloadData)
                .build();

        final List<FrameData> frames = Collections.singletonList(frameData);
        final List<ByteBuffer> frameBuffers = converter.covertToByteBuffer(frames);
        for (ByteBuffer frameBuffer : frameBuffers) {
            try {
                frameBuffer.flip();
                channel.write(frameBuffer);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "WebSocket{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }
}
