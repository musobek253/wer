package com.musobek.wer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@EntityScan(basePackages = ["com.musobek.wer.auth.model"])
@SpringBootApplication
class WerApplication

fun main(args: Array<String>) {
    runApplication<WerApplication>(*args)
}
