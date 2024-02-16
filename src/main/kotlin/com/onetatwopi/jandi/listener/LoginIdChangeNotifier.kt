package com.onetatwopi.jandi.listener

object LoginIdChangeNotifier {
    private var listener: LoginIdChangeListener? = null

    fun setListener(listener: LoginIdChangeListener?) {
        this.listener = listener
    }

    fun notifyLoginIdChanged(newLoginId: String?, isUpdate: Boolean) {
        listener?.onLoginIdChanged(newLoginId, isUpdate = isUpdate)
    }
}