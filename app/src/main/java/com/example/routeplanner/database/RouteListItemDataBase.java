package com.example.routeplanner.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.routeplanner.RouteListItem;

@Database(entities = {RouteListItem.class}, version = 1)
public abstract class RouteListItemDataBase extends RoomDatabase {
    public abstract RouteListItemDAO routeDao();
}
