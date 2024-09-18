package com.example.vulanguageapp.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.vulanguageapp.models.EnrollmentModel;

import java.util.List;

@Dao
public interface EnrollmentDao {

   // @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Upsert
    void insert(EnrollmentModel enrollment);

    @Query("SELECT * FROM enrollments")
    List<EnrollmentModel> getAllEnrollments();

    @Query("DELETE FROM enrollments")
    void clearTable();
}
