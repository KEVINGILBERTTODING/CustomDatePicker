package com.example.customdatepicker;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.customdatepicker.databinding.ActivityMainBinding;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Set<Long> validDates = new HashSet<>();
    private CalendarView calendarView;
    private View selectedView = null; // Menyimpan view yang sedang dipilih
    private long selectedDateMillis = -1; // Menyimpan tanggal dalam milidetik yang dipilih sebelumnya

    private Button btnClear; // Tombol clear selection




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.cvCalendar);



        // Simulasi tanggal yang valid dari API
        String[] apiDates = {"2024-12-02", "2024-12-05", "2024-12-10", "2024-11-11", "2025-01-01"};



        // Mengubah tanggal string API menjadi millisecond dan menambahkannya ke validDates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (String dateStr : apiDates) {
            try {
                Date date = sdf.parse(dateStr);
                // Convert to milliseconds directly
                validDates.add(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Setup untuk kalender
        setupCalendar();

    }


    private void setupCalendar() {
        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(10);
        YearMonth lastMonth = currentMonth.plusMonths(10);

        LocalDate firstDate = firstMonth.atDay(1);
        LocalDate lastDate = lastMonth.atEndOfMonth();

        calendarView.setup(YearMonth.from(firstDate), YearMonth.from(lastDate), DayOfWeek.SUNDAY);
        calendarView.scrollToMonth(YearMonth.from(currentMonth.atDay(1)));

        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(DayViewContainer container, CalendarDay day) {
                container.textView.setText(String.valueOf(day.getDate().getDayOfMonth()));

                long dayInMillis = Date.from(day.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();

                if (validDates.contains(dayInMillis)) {
                    container.textView.setTextColor(getResources().getColor(android.R.color.black));
                    container.textView.setClickable(true);

                    container.textView.setOnClickListener(v -> {
                        if (selectedDateMillis == dayInMillis) {
                            // Unselect jika tanggal sama
                            clearSelection();
                        } else {
                            // Hapus latar belakang sebelumnya
                            if (selectedView != null) {
                                selectedView.setBackgroundColor(getColor(R.color.white));
                            }

                            // Atur latar belakang baru
                            v.setBackground(getDrawable(R.drawable.bg_selected));
                            selectedView = v;
                            selectedDateMillis = dayInMillis;
                            Log.d("selected date", "bind: " + day.getDate().toString());
                        }
                    });
                } else {
                    container.textView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    container.textView.setClickable(false);
                    container.textView.setOnClickListener(null);
                }

            }
        });

        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {

            @Override
            public void bind(@NonNull MonthViewContainer container, CalendarMonth calendarMonth) {
                container.textView.setText(calendarMonth.getYearMonth().getMonth().toString().toUpperCase() + " " + calendarMonth.getYearMonth().getYear());

            }

            @NonNull
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }
        });


    }

    private void setupClearButton() {
//        btnClear.setOnClickListener(v -> clearSelection());
    }

    private void clearSelection() {
        if (selectedView != null) {
            selectedView.setBackgroundColor(Color.WHITE); // Reset background
        }
        selectedView = null;
        selectedDateMillis = -1; // Reset tanggal yang dipilih
        Toast.makeText(this, "Selection Cleared", Toast.LENGTH_SHORT).show();
    }


}