package com.b0r1ngx.lab04

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import name.ank.lab4.BibDatabase
import name.ank.lab4.BibEntry
import name.ank.lab4.Keys
import java.io.InputStream
import java.io.InputStreamReader

/**
 * If you want to infinity loop you're data in
 */
class ArticleAdapter(stream: InputStream, private val repeatInfinity: Boolean = false):
    RecyclerView.Adapter<ArticleAdapter.BibTexViewHolder>() {
    private var database: BibDatabase
    private val Int.current: Int get() = this % database.size()

    init {
        InputStreamReader(stream).use { reader ->
            database = BibDatabase(reader)
        }
    }

    class BibTexViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        private val titleTextView = itemView.findViewById<TextView>(R.id.title)
        private val authorTextView = itemView.findViewById<TextView>(R.id.author)
        private val journalYearTextView = itemView.findViewById<TextView>(R.id.journal_n_year)

        fun bind(entry: BibEntry) {
            val title = parse(entry.getField(Keys.TITLE), 48)
            val author = parse(entry.getField(Keys.AUTHOR), 47)
            val journal = parse(entry.getField(Keys.JOURNAL), 41)

            titleTextView.text = "Title: $title"
            authorTextView.text = "Author: $author"
            journalYearTextView.text = "Journal: $journal, ${entry.getField(Keys.YEAR)}"
        }

        private fun parse(s: String, symbolsLimit: Int) =
            if (s.length > symbolsLimit) "${s.substring(0, symbolsLimit)}..." else s
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibTexViewHolder =
        BibTexViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bibtex_item, parent, false)
        )

    override fun onBindViewHolder(holder: BibTexViewHolder, position: Int) =
        if (repeatInfinity) holder.bind(database.getEntry(position.current))
        else holder.bind(database.getEntry(position))

    override fun getItemCount(): Int =
        if (repeatInfinity) 1488 * database.size() else database.size()
}