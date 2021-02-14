package com.lc.spring_mirai.util

import net.mamoe.mirai.message.data.MarketFace

object DiceUtil {
    /**
     * 骰子
     * @param number 点数
     * @author 19525
     * @Date 2021/2/10 18:33
     */
    fun dice(number: Int): MarketFace {
        val dataClazz = Class.forName("net.mamoe.mirai.internal.network.protocol.data.proto.ImMsgBody${'$'}MarketFace")
        val data = dataClazz.constructors.find { it.parameterTypes.size == 13 }!!.newInstance(
            byteArrayOf(91, -23, -86, -80, -27, -83, -112, 93),
            6,
            1,
            byteArrayOf(72, 35, -45, -83, -79, 93, -16, -128, 20, -50, 93, 103, -106, -73, 110, -31),
            11464,
            3,
            byteArrayOf(52, 48, 57, 101, 50, 97, 54, 57, 98, 49, 54, 57, 49, 56, 102, 57),
            byteArrayOf(),
            0,
            200,
            200,
            byteArrayOf(114, 115, 99, 84, 121, 112, 101, 63, 49, 59, 118, 97, 108, 117, 101, 61, (47 + number).toByte()),
            byteArrayOf(10, 6, 8, -56, 1, 16, -56, 1, 64, 1, 88, 0, 98, 9, 35, 48, 48, 48, 48, 48, 48, 48, 48, 106, 9, 35, 48, 48, 48, 48, 48, 48, 48, 48)
        )
        val diceClazz = Class.forName("net.mamoe.mirai.internal.message.MarketFaceImpl")
        return diceClazz.constructors.find { it.parameterTypes.size == 1 }!!.newInstance(data) as MarketFace
    }

}