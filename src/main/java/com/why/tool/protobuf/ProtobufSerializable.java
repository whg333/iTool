/*
 * Copyright (c) 2011 Yoyo Systems. All rights reserved.
 *
 * $Id$
 */
package com.why.tool.protobuf;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

public interface ProtobufSerializable<T extends GeneratedMessage> {
    T copyTo();

    byte[] toByteArray();

    void parseFrom(byte[] bytes) throws InvalidProtocolBufferException;

    void copyFrom(T message);
}
