package com.example.routeplanner;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RouteListItem.class}, version = 1)
public abstract class RouteListItemDataBase extends RoomDatabase {
    public abstract RouteListItemDAO routeDao();
}
