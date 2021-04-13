package com.b0r1ngx.lab04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val articles = resources.openRawResource(R.raw.articles)
        val viewManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val viewAdapter = ArticleAdapter(articles, true)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = viewAdapter
        recyclerView.layoutManager = viewManager
    }
}