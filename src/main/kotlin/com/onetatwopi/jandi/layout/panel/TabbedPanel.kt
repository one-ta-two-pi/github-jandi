package com.onetatwopi.jandi.layout.panel

import com.intellij.ui.components.JBTabbedPane

class TabbedPanel {

    private var panel = JBTabbedPane()

    fun addTab(contentPanel: ContentPanel) {
        panel.addTab(contentPanel.name, contentPanel.panel)
    }

    fun getPanel(): JBTabbedPane {
        return panel
    }
}