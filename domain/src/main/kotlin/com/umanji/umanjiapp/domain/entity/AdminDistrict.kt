package com.umanji.umanjiapp.domain.entity

/**
 * Data class for representing administrative district.
 *
 * References:
 * 우리나라 행정구역을 영어로.pdf [https://i1.daumcdn.net/svc/attach/U03/blogbook/5291D1B60618700003]
 */
data class AdminDistrict(
        val continent: String? = null,              // 대륙
        val country: String? = null,                // 국가
        val specialCity: String? = null,            // 특별시 e.g. 서울특별시
        val metropolitanCity: String? = null,       // 광역시 e.g. 부산광역시, 광주광역시, etc.
        val province: String? = null,               // 도 e.g. 경기도, 전라남도, 전라북도, etc.
        val city: String? = null,                   // 시 e.g. 성남시, 김포시, etc.
        val county: String? = null,                 // 군 e.g. 함평군, 양양군, etc.
        val district: String? = null,               // 구 e.g. 강남구, 강북구, etc.
        val town: String? = null,                   // 읍 e.g. 함평읍, 양양읍, etc.
        val township: String? = null,               // 면 e.g. 북면, 성남면, etc.
        val neighborhood: String? = null,           // 동 e.g. 금호동, 풍암동 etc.
        val village: String? = null,                // 리 e.g. 기각리, 예내리 etc.
        val countryCode: String? = null,            // for convenience
        val locality: String? = null,               // for convenience
        val subLocality1: String? = null,           // for convenience
        val subLocality2: String? = null,           // for convenience
        val premise: String? = null                 // for convenience
) : Entity
