package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovingObstaclesView extends View {
    private static final int CIRCLE_RADIUS = 100;
    private static final int OBSTACLE_WIDTH = 100;
    private static final int OBSTACLE_HEIGHT = 150, OBSTACLE_HEIGHT1 = 300;
    private static final int OBSTACLE_SPEED = 25;
    private static final int MAX_OBSTACLES = 1;
    private static final int JUMP_VELOCITY = -50;
    private static final int MAX_SPEED_INCREASE = 30;
    private Paint circlePaint;
    private Paint obstaclePaint;
    private int circleX,circleX1;
    private int circleY,circleY1;
    private List<RectF> obstacles;
    private int velocityY;
    private boolean isJumping,isSmallObstacle = true,hasCollision = false;
    public boolean isGameOver;
    private long startTime;
    private int collisionCount = 0;
    private int score = -310;
    private float prevCircleX1 = 0;
    private boolean isDoubleTap = false;
    private int jumpCount = 0;
    private boolean canJump = true;
    private MediaPlayer jumpSound, bgSound;
    private Drawable mario;
    private Drawable chaser;
    private Drawable backgroundDrawable;


    public MovingObstaclesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        obstaclePaint = new Paint();
        obstaclePaint.setColor(Color.BLUE);
        obstacles = new ArrayList<>();
        startTime = System.currentTimeMillis();
        jumpSound = MediaPlayer.create(context, R.raw.jump_sound);
        bgSound = MediaPlayer.create(context, R.raw.arcademusic);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        int spacing = 100;
        circleX = (width - CIRCLE_RADIUS - 400)/2;
        circleY = height - spacing - CIRCLE_RADIUS - 50;

        circleX1 = (width - CIRCLE_RADIUS - 1200)/2;
        circleY1 = height - spacing - CIRCLE_RADIUS - 73;

        generateObstacles(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bgSound.seekTo(0);
        bgSound.start();

        int baselineY = getHeight() - OBSTACLE_HEIGHT;

        Paint fillPaint = new Paint();
        fillPaint.setColor(Color.DKGRAY);
        canvas.drawRect(0, baselineY, getWidth(), getHeight(), fillPaint);

        Paint baselinePaint = new Paint();
        baselinePaint.setColor(Color.BLACK);
        canvas.drawLine(0, baselineY, getWidth(), baselineY, baselinePaint);

        backgroundDrawable = getResources().getDrawable(R.drawable.jailbg);
        backgroundDrawable.setBounds(0, 0, getWidth(), getHeight());
        backgroundDrawable.draw(canvas);

        chaser = getResources().getDrawable(R.drawable.chaser); // Replace with your Mario character drawable
        chaser.setBounds(circleX1 - CIRCLE_RADIUS, circleY1 - CIRCLE_RADIUS, circleX1 + CIRCLE_RADIUS, circleY1 + CIRCLE_RADIUS);
        chaser.draw(canvas);

        mario = getResources().getDrawable(R.drawable.mario1); // Replace with your Mario character drawable
        mario.setBounds(circleX - CIRCLE_RADIUS, circleY - CIRCLE_RADIUS, circleX + CIRCLE_RADIUS, circleY + CIRCLE_RADIUS);
        mario.draw(canvas);

        long elapsedTime = System.currentTimeMillis() - startTime;
        int obstacleSpeed = OBSTACLE_SPEED + (int) (elapsedTime / 2000);
        obstacleSpeed = Math.min(obstacleSpeed, OBSTACLE_SPEED + MAX_SPEED_INCREASE);
        obstacleSpeed += 0.1;

        hasCollision = false;
        int intersectCount = 0;

        if (isGameOver) {
            Toast.makeText(getContext().getApplicationContext(), "collided", Toast.LENGTH_SHORT).show();
            MainActivity.gotoGameOverIntentFn();
            return;
        }

        for (RectF obstacleRect : obstacles) {
            obstacleRect.offset(-obstacleSpeed, 0);

            if (obstacles.indexOf(obstacleRect) % 2 == 0) {
                // Draw a small obstacle
                Drawable smallObstacleDrawable = getResources().getDrawable(R.drawable.brickwalls);
                smallObstacleDrawable.setBounds((int) obstacleRect.left, (int) obstacleRect.top, (int) obstacleRect.right, (int) obstacleRect.bottom);
                smallObstacleDrawable.draw(canvas);
            } else {
                // Draw a tall obstacle
                float top = obstacleRect.top - 50;
                RectF tallObstacleRect = new RectF(obstacleRect.left, top, obstacleRect.right, obstacleRect.bottom);
                Drawable tallObstacleDrawable = getResources().getDrawable(R.drawable.brickwalls);
                tallObstacleDrawable.setBounds((int) tallObstacleRect.left, (int) tallObstacleRect.top, (int) tallObstacleRect.right, (int) tallObstacleRect.bottom);
                tallObstacleDrawable.draw(canvas);
            }

            if (RectF.intersects(obstacleRect, getCircleRect())) {
                obstaclePaint.setColor(Color.BLUE);
                hasCollision = true;
                intersectCount++;
//                Toast.makeText(getContext().getApplicationContext(), "collsion+=1", Toast.LENGTH_SHORT).show();
                circleX1 += 10;
                collisionCount++;
            } else {
                obstaclePaint.setColor(Color.BLUE);
            }

            if (obstacleRect.right < circleX + CIRCLE_RADIUS && hasCollision == false) {
                score += 50;
            }
        }

        score += Math.abs(circleX1 - prevCircleX1);

        mario.draw(canvas);

        for (int i = obstacles.size() - 1; i >= 0; i--) {
            RectF obstacleRect = obstacles.get(i);
            if (obstacleRect.right <= 0) {
                obstacles.remove(i);
            }
        }

        if (obstacles.size() < MAX_OBSTACLES) {
            generateObstacles(getWidth(), getHeight());
        }

        if (isJumping) {
            velocityY += 2.1; // Gravity
            circleY += velocityY;
            circleY1 += velocityY;
            int baselineBottom = getHeight() - OBSTACLE_HEIGHT;
            if (circleY >= baselineBottom - CIRCLE_RADIUS && circleY1 >= baselineBottom - CIRCLE_RADIUS - 20) {
                circleY = baselineBottom - CIRCLE_RADIUS;
                circleY1 = baselineBottom - CIRCLE_RADIUS - 20;
                isJumping = false;
                canJump = true;
            }
        }

        Paint scorePaint = new Paint();
        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(50);
        String scoreText = "Score: " + score;
        float textWidth = scorePaint.measureText(scoreText);
        float x = getWidth() - textWidth - 20;
        float y = 50 + Math.abs(scorePaint.ascent());
        canvas.drawText(scoreText, x, y, scorePaint);

        prevCircleX1 = circleX1;

        if (hasCollision && intersectCount == 2) {
            isGameOver = true;
            MainActivity.gotoGameOverIntentFn();
        }

        invalidate();
    }

    private RectF getCircleRect() {
        return new RectF(circleX - CIRCLE_RADIUS, circleY - CIRCLE_RADIUS, circleX + CIRCLE_RADIUS, circleY + CIRCLE_RADIUS);
    }

    private void generateObstacles(int width, int height) {
        int obstacleY = height - OBSTACLE_HEIGHT - 150;

        Random random = new Random();
        boolean isSmallObstacle = random.nextBoolean();

        if (isSmallObstacle) {
            RectF smallObstacleRect = new RectF(width, obstacleY, width + OBSTACLE_WIDTH, obstacleY + OBSTACLE_HEIGHT);
            obstacles.add(smallObstacleRect);
        } else {
            obstacleY = height - OBSTACLE_HEIGHT1 - 150;
            RectF tallObstacleRect = new RectF(width, obstacleY, width + OBSTACLE_WIDTH, obstacleY + OBSTACLE_HEIGHT1);
            obstacles.add(tallObstacleRect);
        }

        isSmallObstacle = !isSmallObstacle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isJumping) {
            jump();
            playJumpSound();
        }
        return super.onTouchEvent(event);
    }

    private void jump() {
        velocityY = JUMP_VELOCITY;
        isJumping = true;
    }

    public void startMoving() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isGameOver) {
                    invalidate();
                }
            }
        }, 0);
    }

    private void playJumpSound() {
        if (jumpSound != null) {
            jumpSound.seekTo(0);
            jumpSound.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseResources();
    }

    private void releaseResources() {
        if (jumpSound != null) {
            jumpSound.release();
            jumpSound = null;
        }
    }

}