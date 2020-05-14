package com.example.dotsboxes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameView extends View {
    protected static int players = 2;//noOfPlayers playing the game single,2 or 3
    protected static int size = 6;//grid size as chosen by user(3*3)->4,(4*4)->5,(5*5)->6
    public Paint dotPaint, linePaint, boxPaint;//paint for lines,dots and boxes
    public Paint[] boxPaints, linePaints;
    private PointF[][] dot;
    private Line[][] horizontal, vertical;
    private Box[][] box;
    private ArrayList<Line> moves;
    public int[] scores;
    int score = 0;
    private int currentPlayer = 1;
    private GameViewListener listener;

    //constructors
    public GameView(Context context) {
        super(context);
        setStates();
    }

    //Choose attrs as a parameter because it is called from xml layout file
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setStates();
    }

    public void setStates() {
        this.listener = null;
        moves = new ArrayList<>();
        scores = new int[]{0, 0, 0};
        updateScores1();

        //configure paint for dots
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setColor(Color.parseColor("#000000"));
        dotPaint.setStyle(Paint.Style.FILL);

        //configure paint for lines
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor("#AAAEA9"));
        linePaint.setStrokeWidth(getResources().getDisplayMetrics().density * 6f);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        linePaints = new Paint[3];
        //configure linePaint1, linePaint2 and 3
        for (int i = 0; i < 3; i++) {
            linePaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaints[i].setStyle(Paint.Style.STROKE);
            linePaints[i].setStrokeWidth(getResources().getDisplayMetrics().density * 6f);
            linePaints[i].setStrokeCap(Paint.Cap.ROUND);
        }
        linePaints[0].setColor(Color.parseColor("#FB0577"));
        linePaints[1].setColor(Color.parseColor("#03A9F4"));
        linePaints[2].setColor(Color.parseColor("#2ECC71"));

        boxPaints = new Paint[3];
        //configure box paints
        boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boxPaint.setStyle(Paint.Style.FILL);
        boxPaint.setColor(Color.parseColor("#ffffff"));

        //configure boxPaint1 and boxPaint2,3
        for (int i = 0; i < 3; i++) {
            boxPaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            boxPaints[i].setStyle(Paint.Style.FILL);
            boxPaints[i].setColor(Color.parseColor("#FEB4D6"));
        }
        boxPaints[0].setColor(Color.parseColor("#FEB4D6"));
        boxPaints[1].setColor(Color.parseColor("#81D4FA"));
        boxPaints[2].setColor(Color.parseColor("#82E0AA"));

        //initialize all dots
        dot = new PointF[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                dot[i][j] = new PointF(0, 0);

        //initialize vertical lines
        vertical = new Line[size][size - 1];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size - 1; j++)
                vertical[i][j] = new Line(dot[0][0], dot[0][0], linePaint);

        //initialize horizontal lines
        horizontal = new Line[size - 1][size];
        for (int i = 0; i < size - 1; i++)
            for (int j = 0; j < size; j++)
                horizontal[i][j] = new Line(dot[0][0], dot[0][0], linePaint);

        //initialize all boxes
        box = new Box[size - 1][size - 1];
        for (int i = 0; i < size - 1; i++)
            for (int j = 0; j < size - 1; j++) {
                box[i][j] = new Box(boxPaint);
            }

    }

    //override onMeasure so as to give the custom view a height which is equal to layout width
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    //Todo: Localize invalidate call
    //override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();//width of the container

        //set coordinates of dot points in an array
        //16 for padding and 12 for radius->28
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                dot[i][j].x = 28 + i * (width - 56) / (size - 1);
                dot[i][j].y = 28 + j * (width - 56) / (size - 1);
            }

        //configure boxPaints and draw
        for (int i = 0; i < size - 1; i++)
            for (int j = 0; j < size - 1; j++)
                canvas.drawRect(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i][j + 1].y, box[i][j].rectPaint);

        //configure vertical line paints and draw
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size - 1; j++)
                canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i][j + 1].x, dot[i][j + 1].y, vertical[i][j].edgePaint);

        //configure horizontal lines and paints
        for (int i = 0; i < size - 1; i++)
            for (int j = 0; j < size; j++)
                canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i + 1][j].y, horizontal[i][j].edgePaint);

        //drawdots
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                canvas.drawCircle(dot[i][j].x, dot[i][j].y, 12, dotPaint);
    }

    //TODO: To be modified into drag and draw later
    //configure layout to respond to user touch
    //Listener is not needed because the class extends View
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        score = scores[0] + scores[1] + scores[2];
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return true;
        //point user touched
        PointF current = new PointF();
        current.x = event.getX();
        current.y = event.getY();
        //check if it lies on a vertical line
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size - 1; j++) {
                RectF tempLong = new RectF(dot[i][j].x - 16, dot[i][j].y + 16, dot[i][j].x + 16, dot[i][j + 1].y - 16);
                if (tempLong.contains(current.x, current.y) && vertical[i][j].whoCompletedLine == 0) {
                    vertical[i][j].whoCompletedLine = currentPlayer;
                    vertical[i][j].edgePaint = linePaints[currentPlayer - 1];
                    moves.add(vertical[i][j]);
                    if (!boxVerticalManager(currentPlayer - 1, i, j))
                        currentPlayer = (currentPlayer) % players + 1;
                    invalidate();
                    if (scores[0] + scores[1] + scores[2] == (size - 1) * (size - 1))
                        endGame1();
                    break;
                }
            }
        for (int i = 0; i < size - 1; i++)
            for (int j = 0; j < size; j++) {
                RectF tempLat = new RectF(dot[i][j].x + 16, dot[i][j].y - 16, dot[i + 1][j].x - 16, dot[i][j].y + 16);
                if (tempLat.contains(current.x, current.y) && horizontal[i][j].whoCompletedLine == 0) {
                    horizontal[i][j].whoCompletedLine = currentPlayer;
                    horizontal[i][j].edgePaint = linePaints[currentPlayer - 1];
                    moves.add(horizontal[i][j]);
                    if (!boxHorizontalManager(currentPlayer - 1, i, j))
                        currentPlayer = (currentPlayer) % players + 1;
                    invalidate();
                    if (scores[0] + scores[1] + scores[2] == (size - 1) * (size - 1))
                        endGame1();
                    break;
                }
            }
        return true;
    }


    //todo:update scores
    public boolean boxVerticalManager(int who, int i, int j) {
        if (i == 0 && vertical[i][j].whoCompletedLine != 0 && horizontal[i][j].whoCompletedLine != 0 && horizontal[i][j + 1].whoCompletedLine != 0 && vertical[i + 1][j].whoCompletedLine != 0) {
            box[i][j].whoCompletedBox = who + 1;
            scores[who]++;
            box[i][j].rectPaint = boxPaints[who];
            updateScores1();
        }
        if (i == size - 1 && vertical[i][j].whoCompletedLine != 0 && horizontal[i - 1][j].whoCompletedLine != 0 && horizontal[i - 1][j + 1].whoCompletedLine != 0 && vertical[i - 1][j].whoCompletedLine != 0) {
            box[i - 1][j].whoCompletedBox = who + 1;
            box[i - 1][j].rectPaint = boxPaints[who];
            scores[who]++;
            updateScores1();
        }
        if (i > 0 && i < size - 1 && vertical[i][j].whoCompletedLine != 0 && horizontal[i - 1][j].whoCompletedLine != 0 && horizontal[i - 1][j + 1].whoCompletedLine != 0 && vertical[i - 1][j].whoCompletedLine != 0) {
            box[i - 1][j].whoCompletedBox = who + 1;
            box[i - 1][j].rectPaint = boxPaints[who];
            scores[who]++;
            updateScores1();
        }
        if (i > 0 && i < size - 1 && vertical[i][j].whoCompletedLine != 0 && horizontal[i][j].whoCompletedLine != 0 && horizontal[i][j + 1].whoCompletedLine != 0 && vertical[i + 1][j].whoCompletedLine != 0) {
            box[i][j].whoCompletedBox = who + 1;
            box[i][j].rectPaint = boxPaints[who];
            scores[who]++;
            updateScores1();
        }
        return scores[0] + scores[1] + scores[2] > score;
    }

    public boolean boxHorizontalManager(int who, int i, int j) {
        if (j == 0 && vertical[i][j].whoCompletedLine != 0 && horizontal[i][j].whoCompletedLine != 0 && horizontal[i][j + 1].whoCompletedLine != 0 && vertical[i + 1][j].whoCompletedLine != 0) {
            box[i][j].whoCompletedBox = who + 1;
            box[i][j].rectPaint = boxPaints[who];
            scores[who]++;
            updateScores1();
        }
        if (j < size - 1 && j > 0 && vertical[i][j - 1].whoCompletedLine != 0 && horizontal[i][j].whoCompletedLine != 0 && horizontal[i][j - 1].whoCompletedLine != 0 && vertical[i + 1][j - 1].whoCompletedLine != 0) {
            box[i][j - 1].whoCompletedBox = who + 1;
            box[i][j - 1].rectPaint = boxPaints[who];
            scores[who]++;
            updateScores1();
        }
        if (j > 0 && j < size - 1 && vertical[i][j].whoCompletedLine != 0 && horizontal[i][j].whoCompletedLine != 0 && horizontal[i][j + 1].whoCompletedLine != 0 && vertical[i + 1][j].whoCompletedLine != 0) {
            box[i][j].whoCompletedBox = who + 1;
            box[i][j].rectPaint = boxPaints[who];
            scores[who]++;
            updateScores1();
        }
        if (j == size - 1 && vertical[i][j - 1].whoCompletedLine != 0 && horizontal[i][j].whoCompletedLine != 0 && horizontal[i][j - 1].whoCompletedLine != 0 && vertical[i + 1][j - 1].whoCompletedLine != 0) {
            box[i][j - 1].whoCompletedBox = who + 1;
            box[i][j - 1].rectPaint = boxPaints[who];
            scores[who]++;
            updateScores1();
        }
        return scores[0] + scores[1] + scores[2] > score;
    }

    //since the custom view cannot find the text views in MainActivity, define an interface same as an onClickListener for a button
    public interface GameViewListener {
        void updateScores();

        void endGame();
    }

    //listener setter
    public void setGameViewListener(GameViewListener listener) {
        this.listener = listener;
    }

    public void updateScores1() {
        if (listener != null)
            listener.updateScores();
    }

    public void endGame1() {
        if (listener != null)
            listener.endGame();
    }
}


