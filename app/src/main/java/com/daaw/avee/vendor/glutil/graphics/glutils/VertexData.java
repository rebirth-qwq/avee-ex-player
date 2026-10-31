package com.daaw.avee.vendor.glutil.graphics.glutils;

import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes2.dex */
public interface VertexData {
    void bind();

    FloatBuffer buffer();

    VertexData clear();

    void draw(int i, int i2, int i3);

    VertexData flip();

    int getTotalNumComponents();

    int getVertexCount();

    VertexData put(float f);

    VertexData put(float[] fArr, int i, int i2);

    void unbind();
}
