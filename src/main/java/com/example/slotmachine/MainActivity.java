package com.example.slotmachine;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.animation.ObjectAnimator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final int[] symbols = {
            R.drawable.ic_apple, R.drawable.ic_banana, R.drawable.ic_cherry
    };

    private ImageView reel1;
    private ImageView reel2;
    private ImageView reel3;
    private ImageView btnSpin;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private MediaPlayer spinSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reel1 = findViewById(R.id.reel1);
        reel2 = findViewById(R.id.reel2);
        reel3 = findViewById(R.id.reel3);
        btnSpin = findViewById(R.id.btnSpin);

        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSpinSound();
                spinReels();
            }
        });

        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd("Audio/spinSound.mp3");
            spinSound = new MediaPlayer();
            spinSound.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            spinSound.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playSpinSound() {
        if (spinSound != null) {
            spinSound.release(); // Release the previous instance
        }

        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd("Audio/spinSound.mp3");
            spinSound = new MediaPlayer();
            spinSound.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            spinSound.prepare();
            spinSound.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopSpinSound() {
        if (spinSound != null) {
            spinSound.release();
            spinSound = null;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void InstructionClicked(View view) {
        // Create an ObjectAnimator to animate the translationY property
        ImageView instructionImageView = findViewById(R.id.slotMachineInstruction); // Add this line
        ObjectAnimator animator = ObjectAnimator.ofFloat(instructionImageView, "translationY", 0, -instructionImageView.getHeight());
        animator.setDuration(1400); // Set duration for the animation in milliseconds
        animator.start(); // Start the animation

        // Disable click listener to prevent further clicks
        instructionImageView.setOnClickListener(null);
    }

    private void spinReels() {
        btnSpin.setEnabled(false);

        final Random random = new Random();

        final long spinDuration = 4000L;
        final long startTime = System.currentTimeMillis();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsed = currentTime - startTime;

                if (elapsed < spinDuration) {
                    reel1.setImageResource(symbols[random.nextInt(symbols.length)]);
                    reel2.setImageResource(symbols[random.nextInt(symbols.length)]);
                    reel3.setImageResource(symbols[random.nextInt(symbols.length)]);

                    handler.postDelayed(this, 100);
                } else {
                    int result1 = symbols[random.nextInt(symbols.length)];
                    int result2 = symbols[random.nextInt(symbols.length)];
                    int result3 = symbols[random.nextInt(symbols.length)];

                    reel1.setImageResource(result1);
                    reel2.setImageResource(result2);
                    reel3.setImageResource(result3);

                    if (result1 == result2 && result2 == result3) {
                        showToast("Congratulations! EEEOOOWWW!");
                    } else {
                        showToast("THAT'S WEIRD....");
                    }

                    // Stop sound at the end of spinning
                    stopSpinSound();

                    btnSpin.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSpinSound(); // Release MediaPlayer when the activity is destroyed
    }
}
