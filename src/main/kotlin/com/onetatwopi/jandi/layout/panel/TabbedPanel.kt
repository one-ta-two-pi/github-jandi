package com.onetatwopi.jandi.layout.panel

import com.intellij.ui.components.JBTabbedPane
import javax.swing.*

class TabbedPanel {

    private var panel = JBTabbedPane()
    private var addButton = JButton("+")
    private var refreshButton = JButton("Refresh")

    init {
        panel.tabLayoutPolicy = JTabbedPane.SCROLL_TAB_LAYOUT

        val tabComponentPanel = JPanel()
        tabComponentPanel.layout = BoxLayout(tabComponentPanel, BoxLayout.LINE_AXIS)
        tabComponentPanel.add(Box.createHorizontalGlue())
        tabComponentPanel.add(addButton)
        tabComponentPanel.add(refreshButton)

        panel.setTabComponentAt(0, tabComponentPanel)

        addButton.addActionListener {
            // Add your logic here for the add button
            // For example, you can add a new tab
        }

        refreshButton.addActionListener {
            // Add your logic here for the refresh button
            // For example, you can refresh the content of the current tab
        }
    }


    fun addTab(contentPanel: ContentPanel) {
        panel.addTab(contentPanel.name, contentPanel.panel)
    }

    fun getPanel(): JBTabbedPane {
        return panel
    }
}