package com.example.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.demo.view.BackgroundView;
import com.example.demo.view.GameView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.scoreLabel)
    TextView scoreLabel;
    @BindView(R.id.scoreNumber)
    TextView scoreNumber;
    @BindView(R.id.view)
    GameView view;
    GamePresenter gamePresenter;
    @BindView(R.id.background)
    BackgroundView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gamePresenter = new GamePresenter(view,scoreNumber, this);
    }

}
