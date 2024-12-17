package com.example.customdatepicker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendar.view.ViewContainer;

public class MonthViewContainer extends ViewContainer {
    TextView textView;
    public MonthViewContainer(@NonNull View view) {
        super(view);
        textView = view.findViewById(R.id.headerTextView);

    }
}
