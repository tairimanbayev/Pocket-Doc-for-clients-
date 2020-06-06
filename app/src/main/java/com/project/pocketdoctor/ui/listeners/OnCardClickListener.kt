package com.project.pocketdoctor.ui.listeners

interface OnCardClickListener {
    fun onActivate(position: Int)
    fun onClick(position: Int)
    fun onDeleteClick(position: Int)
    fun onEditClick(position: Int)
}