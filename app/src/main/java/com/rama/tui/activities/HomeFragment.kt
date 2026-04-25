package com.rama.tui.activities

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.rama.tui.DatabaseHelper
import com.rama.tui.R
import com.rama.tui.SessionItem
import com.rama.tui.managers.FontManager
import com.rama.tui.managers.SoundManager
import com.rama.tui.widgets.WdButton

class HomeFragment : Fragment() {

    private lateinit var listView: ListView
    private val dbHelper by lazy { DatabaseHelper(activity) }
    private lateinit var db: android.database.sqlite.SQLiteDatabase

    private lateinit var taskNameView: TextView
    private lateinit var timerView: TextView
    private lateinit var nextTaskView: TextView
    private lateinit var globalControllers: LinearLayout
    private lateinit var editButton: WdButton
    private lateinit var playPauseIcon: ImageView

    private val items: MutableList<SessionItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.view_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.task_list)

        SoundManager.init()

        db = dbHelper.writableDatabase
        loadItems()

        listView.post { FontManager.applyToListView(activity, listView) }
    }

    // Helpers

    private fun loadItems() {
        items.clear()
        val sessions = dbHelper.getSessions(db)
        for ((id, name) in sessions) {
            val tasks = dbHelper.getSessionTasks(db, id)
            items.add(SessionItem.Header(id, name, tasks))
            for (task in tasks) items.add(SessionItem.Row(id, task))
        }
    }

    override fun onDestroyView() {
        SoundManager.release()
        dbHelper.close()
        super.onDestroyView()
    }
}
