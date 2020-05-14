package com.example.dotsboxes;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.UUID;

public class Box {
    Paint rectPaint;
    public int whoCompletedBox = 0;

    Box(Paint paint) {
        rectPaint = paint;
    }
}

class Line {
    Paint edgePaint;
    PointF a, b;
    int whoCompletedLine = 0;

    Line(PointF a, PointF b, Paint paint) {
        this.a = a;
        this.b = b;
        edgePaint = paint;
    }
}

class Move {
    public boolean isHorizontal = false;
    public int row = 0, column = 0;
    public boolean isVirtualPlayer = false, isBoxCompleted = false;
    public Paint linePaint;
    public Paint boxPaint;

    public void setPaint() {
        if (isVirtualPlayer) {
            linePaint.setColor(Color.parseColor("#FB0577"));
            boxPaint.setColor(Color.parseColor("#FEB4D6"));
        }
    }
}
