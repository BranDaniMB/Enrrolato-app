package com.enrrolato.enrrolato.objects

class Schedule constructor(
    public val days: HashMap<String, Boolean>,
    public val startDate: String,
    public val endDate: String,
    public val startTime: String,
    public val endTime: String,
    public val isActive: Boolean,
    public val repeat: String,
    public val typeOfAvailability: Boolean
){
}