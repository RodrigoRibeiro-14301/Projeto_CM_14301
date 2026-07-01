package com.example.campo.model

import com.example.campo.R

data class SoccerField(
    val id: String,
    val name: String,
    val location: String,
    val address: String = "",
    val distanceKm: Double,
    val pricePerHour: Int,
    val rating: Double,
    val reviewCount: Int = 0,
    val format: String,
    val surface: String,
    val playersInfo: String = "",
    val description: String = "",
    val imageRes: Int
)

fun sampleFields(): List<SoccerField> = listOf(
    SoccerField(
        id = "1",
        name = "Campo Verde Belém",
        location = "Belém",
        address = "Rua da Junqueira 112, Belém",
        distanceKm = 1.2,
        pricePerHour = 25,
        rating = 4.8,
        reviewCount = 142,
        format = "5v5",
        surface = "Sintético",
        playersInfo = "5v5 (10 jog.)",
        description = "Campo de futebol 5v5 em relvado sintético de última geração, situado junto ao Rio Tejo em Belém. Iluminação LED de alta potência permite jogar até à meia-noite. Vista privilegiada para o Mosteiro dos Jerónimos.",
        imageRes = R.drawable.campo1
    ),
    SoccerField(
        id = "2",
        name = "Arena Parque das Nações",
        location = "Parque das Nações",
        address = "Av. Dom João II 45, Parque das Nações",
        distanceKm = 3.8,
        pricePerHour = 40,
        rating = 4.9,
        reviewCount = 98,
        format = "7v7",
        surface = "Sintético",
        playersInfo = "7v7 (14 jog.)",
        description = "Arena coberta de alta qualidade junto ao rio, com piso sintético de última geração e ambiente de estádio profissional.",
        imageRes = R.drawable.campo2
    ),
    SoccerField(
        id = "3",
        name = "Pitch Alvalade",
        location = "Alvalade",
        address = "Av. dos Estados Unidos da América, Alvalade",
        distanceKm = 2.1,
        pricePerHour = 22,
        rating = 4.6,
        reviewCount = 67,
        format = "5v5",
        surface = "Sintético",
        playersInfo = "5v5 (10 jog.)",
        description = "Campo de bairro com bom ambiente, popular entre grupos que jogam regularmente ao fim de semana.",
        imageRes = R.drawable.campo3
    ),
    SoccerField(
        id = "4",
        name = "Futsal Center Mouraria",
        location = "Mouraria",
        address = "Largo do Terreirinho, Mouraria",
        distanceKm = 0.8,
        pricePerHour = 18,
        rating = 4.7,
        reviewCount = 54,
        format = "Futsal",
        surface = "Indoor",
        playersInfo = "Futsal (10 jog.)",
        description = "Pavilhão indoor no coração da Mouraria, ideal para jogar independentemente do tempo.",
        imageRes = R.drawable.campo4
    ),
    SoccerField(
        id = "5",
        name = "Campo Estrela FC",
        location = "Campo de Ourique",
        address = "Rua Saraiva de Carvalho, Campo de Ourique",
        distanceKm = 4.2,
        pricePerHour = 35,
        rating = 4.5,
        reviewCount = 41,
        format = "7v7",
        surface = "Relva",
        playersInfo = "7v7 (14 jog.)",
        description = "Relva natural bem cuidada, num dos bairros mais simpáticos de Lisboa.",
        imageRes = R.drawable.campo5
    ),
    SoccerField(
        id = "6",
        name = "SportHub Benfica",
        location = "Benfica",
        address = "Estrada de Benfica, Benfica",
        distanceKm = 5.1,
        pricePerHour = 80,
        rating = 4.8,
        reviewCount = 203,
        format = "11v11",
        surface = "Relva",
        playersInfo = "11v11 (22 jog.)",
        description = "Complexo desportivo completo com campo de 11 em relva natural de competição.",
        imageRes = R.drawable.campo6
    )
)