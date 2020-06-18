package com.example.routeplanner.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.routeplanner.RouteListItem;

@Dao
public interface RouteListItemDAO {
    @Insert
    public void insertRouteListItems(RouteListItem... routeListItems);

    @Update
    public void updateRouteListItems(RouteListItem... routeListItems);

    @Delete
    public void deleteRouteListItems(RouteListItem... routeListItems);

    @Query("SELECT * FROM RouteListItem")
    public RouteListItem[] loadAllRoutes();
}
