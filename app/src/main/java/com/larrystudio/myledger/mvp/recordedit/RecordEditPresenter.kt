package com.larrystudio.myledger.mvp.recordedit

import com.larrystudio.myledger.mvp.BaseMvpPresenter
import com.larrystudio.myledger.room.Record

interface RecordEditPresenter: BaseMvpPresenter{

    fun initLedger(dateString: String?, record: Record?)
    fun onDateClicked()
    fun onChangeCategoryClicked()
    fun onSaveClicked()
    fun onOneMoreClicked()
}