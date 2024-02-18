package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
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
                spinReels();
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void spinReels() {
        btnSpin.setEnabled(false);

        final Random random = new Random();

        final long spinDuration = 3000L;
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
                    btnSpin.setEnabled(true);
                }
            }
        });
    }
}
