package com.onetatwopi.jandi.layout.panel

import javax.swing.DefaultListModel

interface MainPanelAdaptor<T> {

    fun generateModel(): DefaultListModel<T>

    fun render()
}