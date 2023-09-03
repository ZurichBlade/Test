package com.kizitonwose.calendar.sample.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.sample.R
import com.kizitonwose.calendar.sample.databinding.Example1CalendarDayBinding
import com.kizitonwose.calendar.sample.databinding.Example1FragmentBinding
import com.kizitonwose.calendar.sample.shared.displayText
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class Example1Fragment : BaseFragment(R.layout.example_1_fragment), HasToolbar {

    override val toolbar: Toolbar?
        get() = null

    override val titleRes: Int = R.string.example_1_title

    private lateinit var binding: Example1FragmentBinding
    private val weekCalendarView: WeekCalendarView get() = binding.exOneWeekCalendar

    private var selectedDate = LocalDate.now()
    private var scrollSelectedDate = LocalDate.now()
    private var today = LocalDate.now()

    private var items: ArrayList<Item> = ArrayList()
    private lateinit var myAdapter: MyAdapter


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStatusBarColorUpdate(R.color.example_1_bg_light)
        binding = Example1FragmentBinding.bind(view)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager


        for (i in 0..10)
            items.add(Item("Item $i", "Description $i"))

        myAdapter = MyAdapter(items)
        binding.recyclerView.adapter = myAdapter

        val daysOfWeek = daysOfWeek()
        binding.legendLayout.root.children.map { it as TextView }
            .forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].displayText()
                textView.setTextColorRes(R.color.example_1_white)
            }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        setupWeekCalendar(startMonth, endMonth, currentMonth, daysOfWeek)


        binding.btnPrevd.setOnClickListener {
            setSelection(selectedDate, selectedDate.minusDays(1))
        }

        binding.btnNextd.setOnClickListener {
            setSelection(selectedDate, selectedDate.plusDays(1))
        }

        val gestureDetector = GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float,
                ): Boolean {
                    val deltaX = e2?.x?.minus(e1?.x ?: 0f) ?: 0f
                    val deltaY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f

                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (deltaX > 0) {
                            // Right swipe
                            onSwipeRight()
                        } else {
                            // Left swipe
                            onSwipeLeft()
                        }
                    }

                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            },
        )

        binding.recyclerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false // Pass the event to other listeners if needed
        }

    }

    private fun onSwipeLeft() {
        // Handle left swipe action
//        Toast.makeText(requireContext(), "Left swipe detected", Toast.LENGTH_SHORT).show()
        binding.btnNextd.performClick()
    }

    private fun onSwipeRight() {
        // Handle right swipe action
//        Toast.makeText(requireContext(), "Right swipe detected", Toast.LENGTH_SHORT).show()
        binding.btnPrevd.performClick()
    }


    private fun setSelection(oldDate: LocalDate, newDate: LocalDate) {
        selectedDate = newDate
        weekCalendarView.notifyDateChanged(selectedDate)
        oldDate.let { weekCalendarView.notifyDateChanged(oldDate) }
        weekCalendarView.smoothScrollToDate(selectedDate)
        refreshListData(selectedDate)
    }

    private fun setupWeekCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class WeekDayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: WeekDay
            val textView = Example1CalendarDayBinding.bind(view).exOneDayText

            init {
                view.setOnClickListener {
                    if (day.position == WeekDayPosition.RangeDate) {
                        setSelection(selectedDate, day.date)
                    }
                }
            }
        }
        weekCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
            override fun create(view: View): WeekDayViewContainer = WeekDayViewContainer(view)

            override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                container.day = data
                bindDate(
                    data.date,
                    container.textView,
                    data.position == WeekDayPosition.RangeDate,
                )
            }
        }
        weekCalendarView.weekScrollListener = { updateTitle() }
        weekCalendarView.setup(
            startMonth.atStartOfMonth(),
            endMonth.atEndOfMonth(),
            daysOfWeek.first(),
        )
        weekCalendarView.scrollToDate(selectedDate)
    }

    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()
        if (isSelectable) {
            when {
                selectedDate == date -> {
                    textView.setTextColorRes(R.color.example_1_bg)
                    textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                }

                today == date -> {
                    textView.setTextColorRes(R.color.example_1_white)
                    textView.setBackgroundResource(R.drawable.example_1_today_bg)
                }

                else -> {
                    textView.setTextColorRes(R.color.example_1_white)
                    textView.background = null
                }
            }
        } else {
            textView.setTextColorRes(R.color.example_1_white_light)
            textView.background = null
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateTitle() {
        val week = weekCalendarView.findFirstVisibleWeek() ?: return
        // In week mode, we show the header a bit differently because
        // an index can contain dates from different months/years.
        val firstDate = week.days.first().date
        val lastDate = week.days.last().date

        val currentWeekDay = selectedDate.dayOfWeek.name

        week.days.filter { (it.date.dayOfWeek.name == currentWeekDay) }.forEach { days ->
            scrollSelectedDate = days.date
        }

        setSelection(selectedDate, scrollSelectedDate)


        if (firstDate.yearMonth == lastDate.yearMonth) {
            binding.exOneYearText.text = firstDate.year.toString()
            binding.exOneMonthText.text = firstDate.month.displayText(short = false)
        } else {
            binding.exOneMonthText.text =
                firstDate.month.displayText(short = false) + " - " + lastDate.month.displayText(
                    short = false,
                )
            if (firstDate.year == lastDate.year) {
                binding.exOneYearText.text = firstDate.year.toString()
            } else {
                binding.exOneYearText.text = "${firstDate.year} - ${lastDate.year}"
            }
        }
    }

    private fun refreshListData(selectedDate: LocalDate) {
        items.clear()
        for (i in 0..10)
            items.add(Item("Item> $i", "Description>> $i$selectedDate"))

        myAdapter.notifyDataSetChanged()
    }

}

// for recyclerview.
data class Item(val title: String, val description: String)

