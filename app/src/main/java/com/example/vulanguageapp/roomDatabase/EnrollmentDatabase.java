package com.example.vulanguageapp.roomDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.vulanguageapp.interfaces.EnrollmentDao;
import com.example.vulanguageapp.models.EnrollmentModel;

@Database(entities = {EnrollmentModel.class}, version = 1)
public abstract class EnrollmentDatabase extends RoomDatabase {

    private static EnrollmentDatabase instance;

    public abstract EnrollmentDao enrollmentDao();

    public static synchronized EnrollmentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            EnrollmentDatabase.class, "enrollment_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
