package com.example.birthdayreminder.ui.newevent;

import android.content.Context;
import android.os.Handler;
import android.widget.ProgressBar;

import com.example.birthdayreminder.CustomApplication;
import com.example.birthdayreminder.base.BasePresenter;
import com.example.birthdayreminder.data.model.Event;

import java.util.Calendar;

public class NewEventActivityPresenter implements BasePresenter {
    private Calendar calendar;
    private NewEventActivityView view;

    public NewEventActivityPresenter(NewEventActivityView view) {
        this.view = view;
    }

    @Override
    public void onClick(Context context, Class newActivity) {
        view.navigateToNewActivity(context, newActivity);
    }

    public void insertContacts(Event event) {
        final Handler handler = new Handler();
        view.showProgressBar();
        Thread backgroundThread = new Thread(() -> {
            CustomApplication.getRepository().insert(event);
            handler.post(() -> view.hideProgressBar());
        });
        backgroundThread.start();
    }

    public void onDateSet(int year, int month, int dayOfMonth) {
        String dateOfBirth = dayOfMonth + "/" + (month + 1) + "/" + year;
        view.setDateText(dateOfBirth);
    }

    public long onDaysLeft(){
        final Calendar todayDate = Calendar.getInstance();
        final Calendar birthdayCountdown = calendar;

        final int monthOfBirthday = birthdayCountdown.get(Calendar.MONTH);
        final int monthToday = todayDate.get(Calendar.MONTH);

        final int dayOfBirthday = birthdayCountdown.get(Calendar.DAY_OF_MONTH);
        final int dayToday = todayDate.get(Calendar.DAY_OF_MONTH);
        birthdayCountdown.set(Calendar.YEAR, todayDate.get(Calendar.YEAR));

        if (monthOfBirthday < monthToday) {
            birthdayCountdown.set(Calendar.YEAR, todayDate.get(Calendar.YEAR) + 1);
        }
        else if (monthOfBirthday == monthToday){
            if(dayOfBirthday < dayToday){
                birthdayCountdown.set(Calendar.YEAR, todayDate.get(Calendar.YEAR) + 1);
            }
        }
        final long daysLeft = (birthdayCountdown.getTimeInMillis() - todayDate.getTimeInMillis()) / 86400000;
        return daysLeft;
    }

    public void onDateClicked() {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        view.displayDatePickerDialog(year, month, day);
    }

    public Calendar onCalendarSet(int year1, int monthOfYear, int dayOfMonth) {
        calendar.set(year1, monthOfYear, dayOfMonth);
        return calendar;
    }

    @Override
    public void onMenuClicked(Context context, Class newActivity) {
        view.navigateToNewActivity(context, newActivity);
    }

    @Override
    public void onDestroy() {
    }
}