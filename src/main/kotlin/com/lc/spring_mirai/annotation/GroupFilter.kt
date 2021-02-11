package com.lc.spring_mirai.annotation


/**
 * 群号筛选，只有在[includeGroupId]之内或者[ignoreGroupId]之外的群消息才会生效
 * 两个属性互斥，请只注明一个属性
 * 若注明两个属性将忽略[ignoreGroupId]属性
 * 若不传参则为ignore[]，即所有群都生效
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class GroupFilter(
        val includeGroupId: LongArray = [],
        val ignoreGroupId: LongArray = []
)