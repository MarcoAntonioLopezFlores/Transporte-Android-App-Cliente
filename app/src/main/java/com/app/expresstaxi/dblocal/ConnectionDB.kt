package com.app.expresstaxi.dblocal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConnectionDB(context:Context) :
    SQLiteOpenHelper(context,"modules.db", null, 1){

    val CHAT = "CREATE TABLE IF NOT EXISTS chat(" +
            "id integer primary key autoincrement," +
            "user text,"+
            "message text,"+
            "seen integer)"

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CHAT)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun registerData(nameTable:String, datos:ContentValues):Boolean{
        val db = this.writableDatabase

        try{
            db.insert(nameTable,null, datos)
            return true
        }catch (e:Exception){
            return false
        }
    }

    fun getData(tabla: String, parametros:Array<String>):Cursor{
        val db = this.writableDatabase
        return db.query(tabla, parametros, null, null, null, null, null)
    }
}